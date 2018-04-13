import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class gNode {
	String value;
	gNode parent;
	ArrayList<gNode> children=new ArrayList();
	Hashtable<String,gNode> scpNodes=new Hashtable();        //node for function
	Stack sck;
	String type="";

		
	public gNode(){
	}
	
	
	
	
	public gNode(String s ){
		this.value=s;
	}
	
	public gNode  copy(){
		gNode re =new gNode();
		re.value=value;
		re.scpNodes=new Hashtable();
		if (children.size()>0){
			re.children=new ArrayList();
			for (int i =0 ;i<children.size();i++){
				re.children.add(children.get(i).copy());
				re.children.get(i).parent=re;
			}
		}
		
		return  re;
	}
} 
