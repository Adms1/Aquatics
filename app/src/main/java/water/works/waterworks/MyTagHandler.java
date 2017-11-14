package water.works.waterworks;

/*This class is for generating list from HTML ul,ol and li tag
 * Author: Abhi H Shah
 * Date: 29/10/2014 */


import android.text.Editable;
import android.text.Html.TagHandler;

import org.xml.sax.XMLReader;

public class MyTagHandler implements TagHandler {
	boolean first= true;
	String parent=null;
	int index=1;
	@Override
	public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
		// TODO Auto-generated method stub
		if(tag.equals("ul")) parent="ul";
//	    else if(tag.equals("ol")) parent="ol";
	    if(tag.equals("li")){
	        if(parent.equals("ul")){
	            if(first){
	                output.append("\t ● ");
	                first= false;
//	                index++;
	            }else{
	                first = true;
	            }
	        }
//	        else{
//	            if(first){
//	                output.append("\t"+index+". ");
//	                first= false;
//	                index++;
//	            }else{
//	                first = true;
//	            }
//	        }   
	    }
	}
	}