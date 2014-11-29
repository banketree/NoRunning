package domain.statisticsDisplay;

import java.util.ArrayList;
import java.util.List;

import ui.statisticsDisplay.viewModel.FriendsListModel;

import com.example.androidui_sample_demo.R;

import foundation.dataService.util.ImageTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class FriendsListAdapter extends BaseAdapter implements SectionIndexer{
	private List<FriendsListModel> list =null;
	private Context mContext;
	private byte[] portrait = null;
	public FriendsListAdapter(Context mContext, List<FriendsListModel> list) {
		this.mContext = mContext;
		this.list = list;
	}
	
	/**
	 * ��ListView���ݷ����仯ʱ,���ô˷���������ListView
	 * @param list
	 */
	public void updateListView(List<FriendsListModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final FriendsListModel mContent = list.get(position);
		if (view == null) {
		view = LayoutInflater.from(mContext).inflate(R.layout.friends_items, null);
		} 
		viewHolder = new ViewHolder();
		viewHolder.importrait=(ImageView) view.findViewById(R.id.im_portrait);
		viewHolder.tvName = (TextView) view.findViewById(R.id.name);
		viewHolder.tvPersonalWord=(TextView) view.findViewById(R.id.tv_personalWord);
		viewHolder.tvEmail=(TextView) view.findViewById(R.id.tv_acountnum);
//		//����position��ȡ���������ĸ��Char asciiֵ
//		int section = getSectionForPosition(position);
//		
//		//�����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
//		if(position == getPositionForSection(section)){
//			//viewHolder.tvLetter.setVisibility(View.VISIBLE);
//			//viewHolder.tvLetter.setText(mContent.getSortLetters());
//		}else{
//			//viewHolder.tvLetter.setVisibility(View.GONE);
//		}
		portrait=list.get(position).getProtrait();
		Bitmap bmProtrait = ImageTools.byteToBitmap(portrait);
		viewHolder.importrait.setImageBitmap(ImageTools.zoomBitmap(
				bmProtrait, 200, 210));
		viewHolder.tvName.setText(this.list.get(position).getName());
		viewHolder.tvPersonalWord.setText(this.list.get(position).getPersonalWord());
		viewHolder.tvEmail.setText(this.list.get(position).getEmail());
		return view;
		
	}
	
		

	


	final static class ViewHolder {
		ImageView importrait;
		TextView tvPersonalWord;
		TextView tvName;
		TextView tvEmail;
	}


	/**
	 * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
	 */
	public int getSectionForPosition(int position) {
		//return list.get(position).getSortLetters().charAt(0);
		return 0;
	}

	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	public int getPositionForSection(int section) {
//		for (int i = 0; i < getCount(); i++) {
//			String sortStr = list.get(i).getSortLetters();
//			char firstChar = sortStr.toUpperCase().charAt(0);
//			if (firstChar == section) {
//				return i;
//			}
//		}
		
		return -1;
	}
	
	/**
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// �������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}