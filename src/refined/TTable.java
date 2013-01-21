package refined;

import java.util.List;

public class TTable {
	private long tid;
	private long uid;
	private long lid;
	private long did;
	private long iid;
	private List<Long> alist;
	
	public TTable(long tid, long uid, long lid, long did, long iid,
			List<Long> alist) {
		super();
		this.tid = tid;
		this.uid = uid;
		this.lid = lid;
		this.did = did;
		this.iid = iid;
		this.alist = alist;
	}
	public long getTid() {
		return tid;
	}
	public void setTid(long tid) {
		this.tid = tid;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getLid() {
		return lid;
	}
	public void setLid(long lid) {
		this.lid = lid;
	}
	public long getDid() {
		return did;
	}
	public void setDid(long did) {
		this.did = did;
	}
	public long getIid() {
		return iid;
	}
	public void setIid(long iid) {
		this.iid = iid;
	}
	public List<Long> getAlist() {
		return alist;
	}
	public void setAlist(List<Long> alist) {
		this.alist = alist;
	}
	public String getAString(){
		StringBuilder sb = new StringBuilder();
		int i = 0 ;
		if(alist!=null && alist.size()>0){
			for(long at : alist){
				sb.append(at);
				i++;
				if(i != alist.size()){
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}
	
}
