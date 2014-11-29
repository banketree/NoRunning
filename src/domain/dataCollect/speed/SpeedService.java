package domain.dataCollect.speed;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import domain.systemManaConfig.User;

import foundation.speed.step.StepAnalyze;
import foundation.speed.step.StepService;
import foundation.speed.step.VcTimeOut;

import android.content.Context;

public class SpeedService implements VcTimeOut{
	private SpeedData nowData = new SpeedData();
	private StepAnalyze mStepAnalyze = new StepAnalyze();
	private ISpeedTimeOut speedTimeOut=null;
	private List<SpeedData> minuteSpeeds=new ArrayList<SpeedData>();

	public void start(User user){
		mStepAnalyze.start(user,this);
	}

	public void stop(){
		// �رն�ʱ��
		mStepAnalyze.stop();
		// �رռ���
		StepService.removeLis();
	}
	
	public void setSpeedTimeOut(ISpeedTimeOut i){
		this.speedTimeOut = i;
	}
	//�ص��ϲ�DataCollectService��SOnTimeOut����
	public void onTimeOut(SpeedData speedData){
		minuteSpeeds.add(speedData);
		speedTimeOut.SOnTimeOut(speedData);
	}
	@Override
	public void getSpeed(SpeedData a) {
		// TODO Auto-generated method stub
		this.nowData.setDistance(a.getDistance());
		this.nowData.setSpeed(a.getSpeed());
		this.nowData.setCalorie(a.getCalorie());
		onTimeOut(a);
	}
	public SpeedData getNowData(){
		return nowData;
	}
	public List<SpeedData> getSpeedDataList(){
		List<SpeedData> speedDataList=new ArrayList<SpeedData>();
		speedDataList.addAll(minuteSpeeds);
		minuteSpeeds.clear();
		return speedDataList;
	}
}
