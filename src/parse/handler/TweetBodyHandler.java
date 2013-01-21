package parse.handler;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import origin.LuyouOrigin;
import parse.TweetParseHandler;
import refined.NewLuyou;
import refined.TTable;
import dao.ITweetDAO;
import dao.TweetDaoImpl;
/**
 * @author ChenJie
 * 负责解析body
 */
public class TweetBodyHandler implements TweetParseHandler{

	ITweetDAO dao = new TweetDaoImpl();
	
	@Override
	public void handle(LuyouOrigin origin) {
		long uid = Long.parseLong(origin.getA());
		
		String tweet = origin.getC();
		long tid = tweetFilter(tweet);
		
		
		String dev = origin.getF();
		long did = devFilter(dev);
		
		String img = origin.getD();
		long iid = imgFilter(img);
		
		List<Long> alist = atFilter(tweet);//待做
		
		long lid = locationFilter(tweet);
		
		if(tid == 0){
			System.out.println("error!!!");
			System.out.println(origin.toString());
			return;
		}
		TTable table = new TTable(tid,uid,lid,did,iid,alist);
		dao.save(table);
	}
	
	private List<Long> atFilter(String tweet){
		if(tweet!=null && tweet.length()>0){
			
//			return dao.save("pic", "img_url", imgUrl);
		}else{
			
		}
		return null;
	}
	
	private long imgFilter(String imgUrl){
		if(imgUrl !=null && imgUrl.length()>0){
			return dao.save("imgs", "img", imgUrl);
		}else
			return 0;
	}
	
	private long locationFilter(String tweet){
		String regex = "我在这里：#.*?#";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(tweet);
		String location = "";
		if (matcher.find()){
		      location = matcher.group();
		      location =  location.substring(location.indexOf("#")+1, location.length()-1);
//		      System.out.println(location);
		}
		if(location!=null && location.length()>0){
			return dao.saveOrNot("locations", "location", location);
		}else{
			return 0;
		}
	}
	
	private long tweetFilter(String body){
		if(body!=null && body.length()>0){
//			body = body.replaceAll("[\uafff-\uefff]+"," ");
//			body = body.replaceAll("[\u1000-\u3fff]+"," ");
			body = NewLuyou.tweetFilter(body);
			return dao.save("tweets", "tweet", body);
		}else
			return 0;
	}
	
	private long devFilter(String dev){
		if(dev!=null && dev.length()>0){
			return dao.saveOrNot("devices", "device", dev);
		}else
			return 0;
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		String str = "哈皮的spa聚会?😜👉💃💃💃👯👯👯@成长贝贝 有木有爽歪歪❔❔❔咱先填饱肚子石器时代一下🐷🐷下次改唱歌喽🌟🎶我在这里：#北京歌剧舞剧院# http://t.cn/SbxSVO";
		System.out.println(str);
//		char[] array = str.toCharArray();
//		
//		for(char bb: array){
//			System.out.print(bb+" ");
//		}
//		System.out.println();
//		TweetBodyHandler th = new TweetBodyHandler();
//		th.locationFilter(str);
		
//		str = str.replaceAll("[^\u4e00-\u9fa5]+", " ");
//		str = str.replaceAll("[^\u4e00-\u9fff]+", " ");
//		str = str.replaceAll("[\x09\x0A\x0D\x20-\x7E] | [\xC2-\xDF][\x80-\xBF]| \xE0[\xA0-\xBF][\x80-\xBF]| [\xE1-\xEC\xEE\xEF][\x80-\xBF]{2}| \xED[\x80-\x9F][\x80-\xBF]| \xF0[\x90-\xBF][\x80-\xBF]{2}  | [\xF1-\xF3][\x80-\xBF]{3}  | \xF4[\x80-\x8F][\x80-\xBF]{2}  ", " ");
		str = str.replaceAll("[\uafff-\uefff]+"," ");
		str = str.replaceAll("[\u1000-\u3fff]+"," ");
		System.out.println(str);
		
		String test = "😜👉";
		byte[] bs = test.getBytes("UTF-8");

		for(byte b : bs){
			System.out.print(Integer.toHexString(b & 0xFF));
	        System.out.print(" ");
		}
		
	}
	
}
