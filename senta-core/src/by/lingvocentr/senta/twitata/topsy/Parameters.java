
package by.lingvocentr.senta.twitata.topsy;

import java.util.List;

public class Parameters{
   	private String allow_lang;
   	private String maxtime;
   	private String mintime;
   	private Number nohidden;
   	private String page;
   	private String perpage;
   	private String q;
   	private String type;

 	public String getAllow_lang(){
		return this.allow_lang;
	}
	public void setAllow_lang(String allow_lang){
		this.allow_lang = allow_lang;
	}
 	public String getMaxtime(){
		return this.maxtime;
	}
	public void setMaxtime(String maxtime){
		this.maxtime = maxtime;
	}
 	public String getMintime(){
		return this.mintime;
	}
	public void setMintime(String mintime){
		this.mintime = mintime;
	}
 	public Number getNohidden(){
		return this.nohidden;
	}
	public void setNohidden(Number nohidden){
		this.nohidden = nohidden;
	}
 	public String getPage(){
		return this.page;
	}
	public void setPage(String page){
		this.page = page;
	}
 	public String getPerpage(){
		return this.perpage;
	}
	public void setPerpage(String perpage){
		this.perpage = perpage;
	}
 	public String getQ(){
		return this.q;
	}
	public void setQ(String q){
		this.q = q;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
}
