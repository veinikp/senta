
package by.lingvocentr.senta.twitata.topsy;

public class Request{
   	private Parameters parameters;
   	private String resource;
   	private String response_type;
   	private String url;

 	public Parameters getParameters(){
		return this.parameters;
	}
	public void setParameters(Parameters parameters){
		this.parameters = parameters;
	}
 	public String getResource(){
		return this.resource;
	}
	public void setResource(String resource){
		this.resource = resource;
	}
 	public String getResponse_type(){
		return this.response_type;
	}
	public void setResponse_type(String response_type){
		this.response_type = response_type;
	}
 	public String getUrl(){
		return this.url;
	}
	public void setUrl(String url){
		this.url = url;
	}
}
