package origin;

import org.apache.lucene.document.Document;

/**
 * @author ChenJie
 *
 */
public class LuyouOrigin extends Origin{
	private String a;//微博人id
	private String b;//
	private String c;//内容
	private String d;//图片
	private String e;//
	private String f;//设备
	private String g;//回复数
	private String h;//转发数
	
	@Override
	public Document toDocument() {
		return null;
	}
	@Override
	public void fromLine(String line) {
		
	}
	public String toString(){
		StringBuilder sb =new StringBuilder();
		sb.append("a:"+a+"\n");
		sb.append("b:"+b+"\n");
		sb.append("c:"+c+"\n");
		sb.append("d:"+d+"\n");
		sb.append("e:"+e+"\n");
		sb.append("f:"+f+"\n");
		sb.append("g:"+g+"\n");
		sb.append("h:"+h+"\n");
		return sb.toString();
	}
	public String toString(String name){
		if(name.equals("a")){
			return a;
		}else if(name.equals("b")){
			return b;
		}else if(name.equals("c")){
			return c;
		}else if(name.equals("d")){
			return d;
		}else if(name.equals("e")){
			return e;
		}else if(name.equals("f")){
			return f;
		}else if(name.equals("g")){
			return g;
		}else if(name.equals("h")){
			return h;
		}else{
			return name;
		}
	}
	
	/**
	 * @return 微博人id
	 * Dec 21, 2012
	 */
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	
	/**
	 * @return 内容
	 * Dec 21, 2012
	 */
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	/**
	 * @return 图片
	 * Dec 21, 2012
	 */
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e;
	}
	/**
	 * @return 设备
	 * Dec 21, 2012
	 */
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	public String getG() {
		return g;
	}
	public void setG(String g) {
		this.g = g;
	}
	public String getH() {
		return h;
	}
	public void setH(String h) {
		this.h = h;
	}
	
	
	

}
