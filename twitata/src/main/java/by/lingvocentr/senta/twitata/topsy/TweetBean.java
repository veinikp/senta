
package by.lingvocentr.senta.twitata.topsy;

import by.lingvocentr.senta.twitata.topsy.Request;
import by.lingvocentr.senta.twitata.topsy.Response;

public class TweetBean{
   	private Request request;
   	private Response response;

 	public Request getRequest(){
		return this.request;
	}
	public void setRequest(Request request){
		this.request = request;
	}
 	public Response getResponse(){
		return this.response;
	}
	public void setResponse(Response response){
		this.response = response;
	}
}
