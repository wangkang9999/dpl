//wk


import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Pattern;



public class dpl {
	 String src;
	 String[] aa=new String[200];
	 
	 static gNode Tree;
	 static ArrayList<String> memy=new ArrayList();
	 Stack sck=new Stack();
	 static String cmd="";
	
	
	public dpl(){
	}
	
	public static String read(String f){
		String ss = "";
		f=getstr(f);
	
		try {
			ss=readFileContent(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ss;
		
	}
	
	static String getType(String s){
		Pattern pattern=Pattern.compile("^[-\\+]?[\\d]*$");   
		if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^") ){
			return "op";
		}
		else if (pattern.matcher(s).matches()){	
				return "int";
		}else if (s.charAt(0)=='\"' && s.charAt(s.length()-1)=='\"'){
			return "str";
		}else {
			return "var";
		}
		
	}
	static int strToint(String s){	return Integer.parseInt(s);  }


	//find the parent node which has variable or function
	public gNode findNode(gNode a,gNode p){
		gNode tag=a;
		String ff=tag.value;
		//System.out.println("find---");
		//System.out.println(ff);
		if (getType(a.value).equals("int")){
			return tag;
		
		}else{
			tag=p;
			while (tag.scpNodes.get(ff)==null){
				if (tag.parent==null){
					System.err.printf("Error:function %s in undefined ",ff);
					System.exit(0);
				}else{
					tag=tag.parent;
				}
			}				
				return  tag.scpNodes.get(ff);

		}
	}

	//get the string from "string"
	static String getstr(String s){
		if (s.isEmpty()){
			System.exit(0);
		}else if(s.substring(0,1).equalsIgnoreCase("\"")){	
			return s.substring(1,s.length()-1);
		}else{	
			return s;
		}
		return"";
	}
	
	// put the token in tree
	  public gNode parse (ArrayList tokens){
		gNode nod=new gNode();
		nod.value="begin";
		gNode current =nod;
		for (int i =0 ;i <tokens.size();i++){
	//		System.out.println(Tree.scope);
			if (tokens.get(i).equals("(")){
				gNode newnode =new gNode("(");
			//	System.out.println(Tree.scope);
				newnode.parent=current;
				current.children.add(newnode);
				current=newnode;
			}else if (tokens.get(i).equals(")")){
				current=current.parent;
			}else{
				gNode newnode=new gNode();
				newnode.value =(String) tokens.get(i);
				newnode.parent=current;
				current.children.add(newnode);			
			}
		}
		if (current!=nod){
			System.err.println("Erroe: need more )");
			System.exit(0);
		}
		return nod;
	}
	
	//dipplay the tree  
	public static   void show(gNode n){
		int lvl=1;
		Stack qqq=new Stack();
		qqq.push(n);
		qqq.push(new gNode("line"));
		gNode t;
		System.out.printf("%d : ",0);
		while (qqq.size()>1){
			t=(gNode) qqq.firstElement();
			qqq.remove(0);
			if (t.value.equals("line")){
				System.out.println("");
				System.out.printf("%d : ",lvl);
				lvl++;
				qqq.push(new gNode("line"));
			}else if (t.children.size()==0){
				System.out.printf("<%-6s:%2d",t.value,t.children.size());
				System.out.print(t.scpNodes);
				System.out.print(">   ");
			}else{
				System.out.printf("<%-6s:%2d",t.value,t.children.size());
				System.out.print(t.scpNodes);
				System.out.print(">   ");
				for (int i=0 ;i<t.children.size();i++){
					qqq.push(t.children.get(i));
				}
			}
			
		}	
		System.out.println("");
	}
	
	//bulid Syntax tree	
	public  gNode sprogram(String str){
		ArrayList<String> tokens=new ArrayList();
		//Lexical Analysis,	
		String[] str1=null;
		str1=str.replace("(", " ( ").replace(")", " ) ").replace("\n", " ").split(" ");
		for (int i =0 ;i <str1.length;i++){
			if (str1[i].length()>0){	
				tokens.add(str1[i]);
			}
		}		

		gNode re=parse(tokens);	
		
		return re;			
	
	}
	
	public static gNode match(String s,gNode n){
		gNode tag=n;
		Hashtable scp=tag.scpNodes;
	//	show(n.parent);
		while (scp.get(s)==null){
			if (tag.parent==null){
				System.err.printf("Error: %s in undefined ",s);
				System.exit(0);
			}else{
				tag=tag.parent;
				scp= tag.scpNodes;
			//	System.out.println(scp);
			}
		}	
		gNode re=(gNode) scp.get(s);
		
		return re;
	}
	
	//recognizing expression===============================================================================
	//and use function to get value
	public static String eval(gNode n){
	//	show(n);
		if (n.children.size()==0){
			if (getType(n.value).equals("int") || getType(n.value).equals("op")){
				return n.value;
			}else if (n.value.equals("cmdline")){
				return cmd;
			}else if (getType(n.value).equals("str")){
				return n.value;
			}else if (n.value.equals("author")){
				return "lzhang47";
			}else if (n.value.equals("begin")){
				return "";
			}else{
			//	System.out.println(match(n.value ,n).type);
				if (match(n.value ,n).type.equals("var")){
					return eval(match(n.value ,n).children.get(2));
				}else{
					return eval(match(n.value ,n));
				}
			//	return eval(match(n.value,n));
			//	return eval(match(n.value ,n).children.get(2));
			}

		}else{
			return function(n);
		}
	}
	
	
	//Function operation--------------------------------------------------------------------------------------
	//define bulidin function here
	public static String function(gNode n){
		if (n.children.get(0).value.equals("(")){	
			if (n.children.get(0).children.size()==0){
				System.err.printf("Error: nothing to be eval");
				System.exit(0);
			}
			else if (n.children.get(0).children.get(0).value.equals("lambda")){
			//	System.out.println("lambda");
				if (n.children.get(0).children.get(1).children.size()!=n.children.size()-1){
					System.err.printf("Error:too few or more paramenters for function %s  ","lambda");
					System.exit(0);
				}else{
					for (int i =0;i<n.children.get(0).children.get(1).children.size();i++){
						n.children.get(0).scpNodes.put(n.children.get(0).children.get(1).children.get(i).value, n.children.get(i+1));
					}
					return eval(n.children.get(0).children.get(2)); 
				}
			
			}else {
			//	System.out.println("eval ++");
				String re="";
				for (int i =0 ; i< n.children.size();i++){
					re= eval(n.children.get(i));
				}
				return re;
			}
			
		}else if(n.children.get(0).value.equals("+")){
			if (n.children.size()<2){
				System.err.printf("Error:too few paramenters for function %s  ",n.children.get(0).value);;
				System.exit(0);
			}
			//System.out.println(n.children.get(1).children.size());
			//System.out.println(n.children.get(1).value);
			
			int re=Integer.parseInt(eval(n.children.get(1)));

			for (int i=2;i<n.children.size();i++){
				re+=Integer.parseInt(eval(n.children.get(i)));
			}
			return String.valueOf(re);
		}else if(n.children.get(0).value.equals("-")){
			if (n.children.size()<2){
				System.err.printf("Error:too few paramenters for function %s  ",n.children.get(0).value);;
				System.exit(0);
			}else if (n.children.size()==2){
				return String.valueOf(0-Integer.parseInt(eval(n.children.get(1))));
			}
			int re=Integer.parseInt(eval(n.children.get(1)));

			for (int i=2;i<n.children.size();i++){
				re-=Integer.parseInt(eval(n.children.get(i)));
			}
			return String.valueOf(re);
		}else if(n.children.get(0).value.equals("*")){
			if (n.children.size()<1){
				System.err.printf("Error:too few paramenters for function %s  ",n.children.get(0).value);;
				System.exit(0);
			}
			int re=Integer.parseInt(eval(n.children.get(1)));

			for (int i=2;i<n.children.size();i++){
				re*=Integer.parseInt(eval(n.children.get(i)));
			}
			return String.valueOf(re);
		}else if(n.children.get(0).value.equals("/")){
			if (n.children.size()<1){
				System.err.printf("Error:too few paramenters for function %s  ",n.children.get(0).value);;
				System.exit(0);
			}
			int re=Integer.parseInt(eval(n.children.get(1)));

			for (int i=2;i<n.children.size();i++){
				re/=Integer.parseInt(eval(n.children.get(i)));
			}
			return String.valueOf(re);
		}else if(n.children.get(0).value.equals("^")){
			if (n.children.size()!=3){
				System.err.printf("Error:too few paramenters for function %s  ",n.children.get(0).value);;
				System.exit(0);
			}
			int re=Integer.parseInt(eval(n.children.get(1)));
			int re2=1;
			for (int i=2;i<n.children.size();i++){
				for (int j=0;j<Integer.parseInt(eval(n.children.get(i)));j++){
					re2=re*re2;		
				}
			}
			return String.valueOf(re2);
		}else if(n.children.get(0).value.equals("def")){
			//System.out.println("eval def");
			gNode p=n.parent;
			if (n.children.get(1).children.size()==0){
				n.type="var";
				p.scpNodes.put(n.children.get(1).value, n);
				
			}else{
				n.type="func";
				p.scpNodes.put(n.children.get(1).children.get(0).value, n);
				
			}
			return "";

			
		}else if(n.children.get(0).value.equals("if")){
			if (n.children.size()!=4){
				System.err.printf("Error:too few or more paramenters for function %s  ","if");
				System.exit(0);
			}else{
				if (eval(n.children.get(1)).equals("1")){
					return eval(n.children.get(2));
				}else{
					return eval(n.children.get(3));
				}
			}
			return "";
			
		}else if(n.children.get(0).value.equals("for")){
			String var=n.children.get(1).children.get(0).value;
			String begin=eval(n.children.get(1).children.get(1));
			String end=eval(n.children.get(1).children.get(2));
			String step=eval(n.children.get(1).children.get(3));
			n.scpNodes.put(var,new gNode(""));
			for (int i=Integer.parseInt(begin); i<Integer.parseInt(end);i=i+Integer.parseInt(step)){
				n.scpNodes.put(var, new gNode(String.valueOf(i)));
			//	System.out.println(n.scpNodes.get(var));
				eval(n.children.get(2));				
			}			
			return "";
		}else if(n.children.get(0).value.equals("array")){
			ArrayList xx=new ArrayList();
			for (int i=1; i<n.children.size();i++){
				xx.add(eval(n.children.get(i)));
			}
			return xx.toString();
			
		}else if(n.children.get(0).value.equals("get")){
			//arrays with O(1) access time
			gNode ary=match(n.children.get(1).value,n);			
		//	System.out.println(ary.children.get(1).value);
			return ary.children.get(2).children.get(Integer.parseInt(   eval(  n.children.get(2)  ))+1).value;
			
		}else if(n.children.get(0).value.equals("print")){
			if (n.children.size()!=2){
				System.err.printf("Error:too few or more paramenters for function %s  ","print");
				System.exit(0);
			}else{
				System.out.println(eval(n.children.get(1)));
				return "print-done";
			}
			
			
		}else  if(n.children.get(0).value.equals("<")){
			if (n.children.size()<3){
				System.err.printf("Error:too few paramenters for function %s  ","<");
				System.exit(0);
			}else if(n.children.size()>3){
				System.err.printf("Error:too more paramenters for function %s  ","<");
				System.exit(0);
			}else{
				if(getType(eval(n.children.get(1))).equals("int") && getType(eval(n.children.get(2))).equals("int")){
					if (strToint(eval(n.children.get(1))) < strToint(eval(n.children.get(1)))){
						return "1";
					}
				}	
				if(getType(eval(n.children.get(1))).equals("str") && getType(eval(n.children.get(2))).equals("str")){
					if (eval(n.children.get(1)).compareTo(eval(n.children.get(1)))<0){
						return "1";
						}
				}
			return "0";	
			}
			
		}else  if(n.children.get(0).value.equals("=")){
			if (n.children.size()<3){
				System.err.printf("Error:too few paramenters for function %s  ","=");
				System.exit(0);
			}else if(n.children.size()>3){
				System.err.printf("Error:too more paramenters for function %s  ","=");
				System.exit(0);
			}else{
				if(getType(eval(n.children.get(1))).equals("int") && getType(eval(n.children.get(2))).equals("int")){
					if (strToint(eval(n.children.get(1))) == strToint(eval(n.children.get(2)))){
						return "1";
						}
					}
						
				if(getType(eval(n.children.get(1))).equals("str") && getType(eval(n.children.get(2))).equals("str")){
					if (eval(n.children.get(1)).compareTo(eval(n.children.get(1)))==0){
						return "1";
					}
				}
			return "0";		
			}
		
		}else  if(n.children.get(0).value.equals(">")){
			if (n.children.size()<3){
				System.err.printf("Error:too few paramenters for function %s  ",">");
				System.exit(0);
			}else if(n.children.size()>3){
				System.err.printf("Error:too more paramenters for function %s  ",">");
				System.exit(0);
			}else{
				if(getType(eval(n.children.get(1))).equals("int") && getType(eval(n.children.get(2))).equals("int")){
					if (strToint(eval(n.children.get(1))) > strToint(eval(n.children.get(1)))){
					return "1";
					}
				}
								
				if(getType(eval(n.children.get(1))).equals("str") && getType(eval(n.children.get(2))).equals("str")){
					if (eval(n.children.get(1)).compareTo(eval(n.children.get(1)))>0){
						return "1";
					}	
				}
				return "0";		
			}
						
		}else if(n.children.get(0).value.equals("Stack")){
			Stack ss=new Stack();
			for (int i=1;i<n.children.size();i++){
				ss.push(eval(n.children.get(i)));
			}
			n.sck=ss;
		//	System.out.println(n);
			return ss.toString();
		
		}else if(n.children.get(0).value.equals("Spop")){
			
			gNode tag=match(n.children.get(1).value,n).children.get(2);
			int l=tag.children.size();
			if (l==1){
				System.err.println("stack is empty");
				System.exit(0);
			}
			String last=eval(tag.children.get(l-1));
			tag.children.remove(l-1);
			return last;
			
		}else if(n.children.get(0).value.equals("Spush")){
			gNode tag=match(n.children.get(1).value,n).children.get(2);
			String vl=eval(n.children.get(2));	
			gNode nd=new gNode(vl);
			tag.children.add(nd);
			nd.parent=tag;		
			return vl;
			
		}else if(n.children.get(0).value.equals("size")){
			gNode tag=match(n.children.get(1).value,n).children.get(2);
			return String.valueOf( tag.children.size()-1);
			
		}else if(n.children.get(0).value.equals("rpn")){
			String op =eval(n.children.get(1));
			String b =eval(n.children.get(2));
			String a =eval(n.children.get(3));
			int re =0;
			if (op.equals("+")){
				re=strToint(a) + strToint(b);			
			}else if (op.equals("-")){
				re=strToint(a) - strToint(b);	
			}else if (op.equals("*")){
				re=strToint(a) * strToint(b);	
			}else if (op.equals("/")){
				re=strToint(a) / strToint(b);	
			}else if (op.equals("^")){
				re=1;
				for (int i=0 ;i<strToint(b);i++){
					re=re *strToint(a);
				}
			}
			
			return String.valueOf(re);
		}else if(n.children.get(0).value.equals("isnumber?")){
			String item=eval(n.children.get(1));
			if (getType(item).equals("int")){
				return "1";
			}else {
				return "0";
			}
			
		}else if(n.children.get(0).value.equals("readfile")){
			if (n.children.size()<2){
				System.err.printf("Error:too few or more paramenters for function %s  ","if");
				System.exit(0);
			}else{
				String str=read(eval(n.children.get(1)));
				String[] str1=null;
				str1=str.replace("(", " ( ").replace(")", " ) ").replace("\n", " ").split(" ");
				for (int i =0 ;i <str1.length;i++){
					if (str1[i].length()>0){	
						memy.add(str1[i]);
					}
				}	
				
				return read(eval(n.children.get(1)));
			}
		}else if(n.children.get(0).value.equals("fsize")){
			
			return String.valueOf(memy.size());
			

		}else if(n.children.get(0).value.equals("fget")){
			return memy.get(Integer.parseInt(eval(n.children.get(1))));
			
		}else{
			if (n.children.get(0).children.size()==0){
			//	System.out.println("eval f");
				gNode func=match(n.children.get(0).value,n).copy();
				//=====================================================================================================================

				if(func.children.size()==0){
					func=match(func.value,n).copy();
				}
				
				func.parent=n;
				if (func.children.get(1).children.size() !=n.children.size()){
					System.err.printf("Error:too few or more paramenters for function %s  ",n.children.get(0).value);
					System.exit(0);
				}
				for (int i=1;i< n.children.size();i++){
					func.scpNodes.put(func.children.get(1).children.get(i).value, n.children.get(i));
				}
			//	func.scpNodes.put("aaa", new gNode ("xx"));
			//	System.out.println(match("x",func).value);
			//	show(func);
			//	show(func.children.get(2));
				return eval(func.children.get(2));
			}
		}
		return"not done";
	}
	
	
	public static void main (String[] args) throws IOException {

		
		dpl code=new dpl();
	 	if(args.length==0){
			System.err.println("Error:  need inputfile");
			System.exit(0);
		}else if (args.length>=2){
			cmd=args[1];
		}
	 	
		String src= code.readFileContent(args[0]);	

		gNode Tree=code.sprogram(src);

		
	//	code.show(Tree);	
		String result=eval(Tree);
		System.out.println(result);
	//	System.out.println("done");
	//	code.show(Tree);	
		
	//	gNode cp=Tree.copy();
		
	//	code.show(cp);
		return;
	}

	// readfile 
	private static  String readFileContent(String filename) throws IOException {
		File file = new File(filename);
		if (! file.exists()){
			System.err.println("file does not exist");
			System.exit(0);
		}	
		BufferedReader bf = new BufferedReader(new FileReader(file));
		String content = "";
		StringBuilder sb = new StringBuilder();
		while(content != null){
			content = bf.readLine();
			if(content == null){
				break;
			}
			if (content.indexOf(';')>0){
				content =content.substring(0,content.indexOf(';'));
			}
			sb.append(content.trim());
			sb.append(" ");
		}
  
		bf.close();
		return sb.toString();
	}
}
