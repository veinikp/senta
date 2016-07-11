<%@page import="java.util.List"%>
<%@page import="by.lingvocentr.senta.twitata.ClassifiedBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
        <title>Senta</title>
        <style type="text/css">
            A {

            }
            TABLE {
                width: 90%;  
                border: 0px solid black;
                color: black;
            }
            TD, TH {
                font-family: "gill sans", "new baskerville", sans-serif;
                text-align: left;
                padding: 3px;  
                border: 0px solid black;
            }
            TH {
                color: black;  

                border-bottom: 0px double black;
            }
            TD {
                valign: top;
            }
            .pos {
                color: #006600;
                text-align: left;
/*
                background: url("/twitter.jpg") right;
                background-repeat: no-repeat;
*/
            }
            .neutral {  
                color: #666666;
                text-align: left;
/*
                background: url("/twitter.jpg") right;
                background-repeat: no-repeat;
*/
            }
            .neg { 
                color: #CC0033;
                text-align: left;
/*
                background: url("/twitter.jpg") right;
                background-repeat: no-repeat;
*/
            }
            .scor {
                font-family: Verdana;
                font-size: 10pt;
                color: gray;
            }
        </style>
    </head> 
    <body>

        <div align="center">
            <form action="/twitata/index.jsp" method="GET">
                <input type="text" value="" id="q" name="q" align="center" style="height: 20px;font-size: 16pt;width: 40%"/>
                <input type="submit" value="senta!"/>
            </form>
        </div>
        <hr />
        <% if (request.getAttribute("negative") != null) {%>
        <p align="center">Отзывы по запросу <b><%= session.getAttribute("q")%></b> за последние 10 дней, всего
            <%= ((List<ClassifiedBean>) request.getAttribute("positive")).size() +
                    ((List<ClassifiedBean>) request.getAttribute("neutral")).size() +
                    ((List<ClassifiedBean>) request.getAttribute("negative")).size()%>. <a href="/twitata?csv">Скачать csv.</a>
        </p>
        <p>
            Твиты собраны за <%= request.getAttribute("twit_loading_time")%> с, обработаны за <%= request.getAttribute("classifying_time")%> с.
        </p>
        <p align="left"><img src="/twitata?pieChart" alt="pie chart"><img src="/twitata?daylyChart" alt="dayly chart"></p>
        <table border="0" align="center">

            <caption>
                <th width=33% align="center"><h2 style="font-size: 22pt; text-align: center;color: #006600;">
                    <%= ((List<ClassifiedBean>) request.getAttribute("positive")).size()%> положительныx</h2></th>
                <th width=33% align="center"><h2 style="font-size: 22pt; text-align: center;color: #666666;">
                    <%= ((List<ClassifiedBean>) request.getAttribute("neutral")).size()%> нейтральных</h2></th>
                <th width=33% align="center"><h2 style="font-size: 22pt; text-align: center;color: #CC0033;">
                    <%= ((List<ClassifiedBean>) request.getAttribute("negative")).size()%> отрицательных</h2></th>
            </caption>
            <tr>
                <td valign=top>
                    <table border="0">
                        <% for (ClassifiedBean bean : (List<ClassifiedBean>) request.getAttribute("positive")) {%>
                        <tr><td width=64 valign="top"><img style="width: 64px; height: 64;" src="<%= bean.getUrlUserImage()%>"></td>
                            <td class="pos" valign="top"><a style="text-decoration: none; color: #006600;" href="<%= bean.getUrlContent()%>"><%= bean.getContent()%></a><div class="scor"><%= bean.getScore()%></div></td>
                        </tr>
                        <% }%>
                    </table>
                </td>
                <td  valign=top>
                    <table border="0">
                        <% for (ClassifiedBean bean : (List<ClassifiedBean>) request.getAttribute("neutral")) {%>

                        <tr><td width=64 valign="top"><img style="width: 64px; height: 64;" src="<%= bean.getUrlUserImage()%>"></td>
                            <td class="neutral" valign="top"><a style="text-decoration: none; color: #666666;" href="<%= bean.getUrlContent()%>"><%= bean.getContent()%></a><div class="scor"><%= bean.getScore()%></div></td>
                        </tr>
                        <% }%>
                    </table border="0">
                </td>
                <td class="neg" valign=top>
                    <table>
                        <% for (ClassifiedBean bean : (List<ClassifiedBean>) request.getAttribute("negative")) {%>
                        <tr><td width=64 valign="top"><img style="width: 64px; height: 64;" src="<%= bean.getUrlUserImage()%>"></td>
                            <td class="neg" valign="top"><a style="text-decoration: none; color: #CC0033;" href="<%= bean.getUrlContent()%>"><%= bean.getContent()%></a><div class="scor"><%= bean.getScore()%></div></td>
                        </tr>
                        <% }%>
                    </table>

                </td>
            </tr>
        </table>
        <%}%>
    </body>
</html>