package by.lingvocentr.senta.twitata.servlet;

import by.lingvocentr.senta.twitata.ClassifiedBean;
import by.lingvocentr.senta.twitata.SentimentAnalyzer;
import by.lingvocentr.senta.twitata.SentimentTag;
import by.lingvocentr.senta.twitata.Service;
import com.csvreader.CsvWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author klim
 */
public class QueryController extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
        new SentimentAnalyzer();
        String query = request.getParameter("q");
        String csv = request.getParameter("csv");
        String pieChart = request.getParameter("pieChart");
        String daylyChart = request.getParameter("daylyChart");
        if (query == null && csv == null && pieChart == null && daylyChart == null) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        if (query!=null) {
            query = new String(query.getBytes("ISO-8859-1"));
            System.out.println("q = " + query);
            Map<String,Object> map = Service.processRequest(query);
            request.getSession().setAttribute("map", map);
            System.out.println("loaded and classified");
            request.setAttribute("neutral", map.get(SentimentTag.NEUTRAL.name()));
            request.setAttribute("positive", map.get(SentimentTag.POSITIVE.name()));
            request.setAttribute("negative", map.get(SentimentTag.NEGATIVE.name()));
            request.setAttribute("twit_loading_time", map.get("twit_loading_time"));
            request.setAttribute("classifying_time", map.get("classifying_time"));
            request.getSession().setAttribute("q", query);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        if (csv!=null) {
            System.out.println("creating csv file");
            response.setContentType("application/csv");
            String fileName = "report.csv";
            try {
                fileName = new URI(null, null, "отзывы_" + request.getSession().getAttribute("q") + ".csv", null).toASCIIString();
            } catch (Throwable e) {
                System.err.println(e.getMessage());
            }
            response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\"" );
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
            CsvWriter w = new CsvWriter(writer, '|');
            w.writeRecord(new String[] {"text", "sentiment", "source"});
            Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute("map");
            for (ClassifiedBean bean : ((List<ClassifiedBean>) map.get(SentimentTag.POSITIVE.name()))) {
                w.writeRecord(new String[]{bean.getContent(), SentimentTag.POSITIVE.name(), bean.getSocialNetType()});
            }
            for (ClassifiedBean bean : ((List<ClassifiedBean>) map.get(SentimentTag.NEUTRAL.name()))) {
                w.writeRecord(new String[]{bean.getContent(), SentimentTag.NEUTRAL.name(), bean.getSocialNetType()});
            }
            for (ClassifiedBean bean : ((List<ClassifiedBean>) map.get(SentimentTag.NEGATIVE.name()))) {
                w.writeRecord(new String[]{bean.getContent(), SentimentTag.NEGATIVE.name(), bean.getSocialNetType()});
            }
            w.close();
            System.out.println("csv file sent");
            return;
        }
        if (pieChart!=null) {
            DefaultPieDataset dataset = new DefaultPieDataset();
            Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute("map");
            int p = ((List<ClassifiedBean>) map.get(SentimentTag.POSITIVE.name())).size();
            int neu = ((List<ClassifiedBean>) map.get(SentimentTag.NEUTRAL.name())).size();
            int neg = ((List<ClassifiedBean>) map.get(SentimentTag.NEGATIVE.name())).size();
            if (p>0) {
                dataset.setValue("положительные", p);
            }
            if (neu>0) {
               dataset.setValue("нейтральные", neu);
            }
            if (neg > 0) {
               dataset.setValue("отрицательные", neg);
            }

            boolean legend = false;
            boolean tooltips = false;
            boolean urls = false;

            JFreeChart chart = ChartFactory.createPieChart("", dataset, legend, tooltips, urls);
            if (p>0) {
                ((PiePlot) chart.getPlot()).setSectionPaint("положительные", Color.green);
            }
            if (neu>0) {
                ((PiePlot) chart.getPlot()).setSectionPaint("нейтральные", Color.gray);
            }
            if (neg > 0) {
                ((PiePlot) chart.getPlot()).setSectionPaint("отрицательные", Color.red);
            }
            ((PiePlot) chart.getPlot()).setShadowXOffset(0.0);
            ((PiePlot) chart.getPlot()).setShadowYOffset(0.0);
            chart.getPlot().setBackgroundPaint(new Color(0, 0, 0, 0));
            chart.getPlot().setOutlineStroke(new BasicStroke(0.0f));
            chart.getPlot().setOutlinePaint(Color.white);
            chart.setBorderPaint(Color.GREEN);
            chart.setBorderStroke(new BasicStroke(5.0f));
            chart.setBorderVisible(false);
//            chart.removeLegend();
            int width = 550;
            int height = 250;
            response.setContentType("image/png");
            ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, width, height);
            return;
        }
        if (daylyChart!=null) {
            response.setContentType("image/png");
            XYSeriesCollection xyDataset = new XYSeriesCollection();
            Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute("map");
            int p = ((List<ClassifiedBean>) map.get(SentimentTag.POSITIVE.name())).size();
            int neu = ((List<ClassifiedBean>) map.get(SentimentTag.NEUTRAL.name())).size();
            int neg = ((List<ClassifiedBean>) map.get(SentimentTag.NEGATIVE.name())).size();
            List<EnumMap<SentimentTag, Integer>> dateRange = (List<EnumMap<SentimentTag, Integer>>) map.get("hist");
            System.out.println("hist: " + dateRange.size());
            if (p>0) {
                XYSeries posSeries = new XYSeries("положительные");
                for (int i = 0; i < dateRange.size(); i++) {
                    EnumMap<SentimentTag, Integer> enumMap = dateRange.get(i);
                    posSeries.add(i+1, enumMap.get(SentimentTag.POSITIVE));
                }
                xyDataset.addSeries(posSeries);
            }
            if (neu>0) {
                XYSeries neuSeries = new XYSeries("нейтральные");
                for (int i = 0; i < dateRange.size(); i++) {
                    EnumMap<SentimentTag, Integer> enumMap = dateRange.get(i);
                    neuSeries.add(i + 1, enumMap.get(SentimentTag.NEUTRAL));
                }
                xyDataset.addSeries(neuSeries);
            }
            if (neg > 0) {
              XYSeries negSeries = new XYSeries("отрицательные");
                for (int i = 0; i < dateRange.size(); i++) {
                    EnumMap<SentimentTag, Integer> enumMap = dateRange.get(i);
                    negSeries.add(i+1, enumMap.get(SentimentTag.NEGATIVE));
                }
                xyDataset.addSeries(negSeries);
            }
            
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "", "", "",
                    xyDataset, PlotOrientation.VERTICAL, false, false, false);
            chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.green);
            chart.getXYPlot().getRenderer().setSeriesPaint(1, Color.gray);
            chart.getXYPlot().getRenderer().setSeriesPaint(2, Color.red);
            chart.getXYPlot().getRangeAxis().setVisible(false);
            chart.getPlot().setBackgroundPaint(new Color(0, 0, 0, 0));
            chart.getPlot().setOutlineStroke(new BasicStroke(0.0f));
            chart.setBorderPaint(Color.GREEN);
            chart.setBorderStroke(new BasicStroke(0.0f));
            chart.setBorderVisible(false);
            chart.removeLegend();
            ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 550, 250);
            return;
        }
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
