package test;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class TestIK {

	public static void main(String[] args) throws IOException{
		String str = "巴黎三城眼镜(苏州大润发苏福销售点)";
		StringReader reader = new StringReader(str);
		IKSegmenter ik_false =new IKSegmenter(reader,false);
		Lexeme lex = null;
		System.out.println("ik_false 分词结果：");
		while((lex = ik_false.next()) !=null){
			System.out.print(lex.getLexemeText()+" ");
		}
		
		StringReader reader2 = new StringReader(str);
		IKSegmenter ik_true =new IKSegmenter(reader2,true);
		lex = null;
		System.out.println("\nik_true 分词结果：");
		while((lex = ik_true.next()) !=null){
			System.out.print(lex.getLexemeText()+" ");
		}
	}
	
}
