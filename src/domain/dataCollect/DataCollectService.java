package domain.dataCollect;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.R.bool;
import android.util.Log;
import android.widget.Toast;

import com.File.TxtFileUtil;
import com.baidu.location.f;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import domain.dataCollect.heart.HeartRateData;
import domain.dataCollect.heart.HeartRateService;
import domain.dataCollect.heart.IHeartRateTimeOut;

import domain.dataCollect.speed.ISpeedTimeOut;
import domain.dataCollect.speed.SpeedData;
import domain.dataCollect.speed.SpeedService;

import domain.systemManaConfig.*;
import foundation.dataService.DataCollectDataService;
import foundation.dataService.base.DataContext;
import foundation.dataService.util.DateService;
import foundation.dataService.util.LoadingDialog;
import foundation.webservice.ConnetNet;
import foundation.webservice.MemoWebPara;
import foundation.webservice.WebServiceDelegate;
import foundation.webservice.WebServiceUtils;

public class DataCollectService implements IHeartRateTimeOut, ISpeedTimeOut{

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ConfigurationService configurationService;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */

	private File fv = new File("/sdcard/Speeds.txt"/** �ļ�·���� **/
	);
	private SpeedService speedService;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	// �ļ�
	private File fh = new File("/sdcard/HeartRates.txt"/** �ļ�·���� **/
	);
	private HeartRateService heartRateService;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	// private MinuteSportData minuteSportData;

	public OneSport oneSport;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * �õ�ÿ���Ӳɼ�����(����ֵList&lt;MinuteSpartData&gt;)
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private WebServiceUtils webService;
	private DataCollectDataService dataCollectDataService;
	private CurrentSportData currentSportData = new CurrentSportData();
	private double tempDistance = 0.0;// ��ʱ���ڼ����ܾ���
	private double tempTotalCalorie = 0.0;// ��ʱ���ڼ����ܿ�·��

	private IDataCollectTimeOut dataCollectTimeOut = null;// UI����
	private DataContext dtx;

	public void setDataCollectTimeOut(IDataCollectTimeOut dataCollectTimeOut) {
		this.dataCollectTimeOut = dataCollectTimeOut;
	}

	public DataCollectService() {
		// ʵ����������
		dataCollectDataService = new DataCollectDataService();
		heartRateService = new HeartRateService();
		speedService = new SpeedService();
		heartRateService.setListener(this);// ����heartRateService������ΪDataCollectService
		speedService.setSpeedTimeOut(this);// ����speedService������ΪDataCollectService
		currentSportData = new CurrentSportData();
	}

	public List<MinuteSportData> getMinuteSportData(Date startTime, Date endTime) {
		// begin-user-code
		// TODO �Զ����ɵķ������
		return null;
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param count
	 * @param date
	 * @return
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private OneSport getSportByCount(Integer count, String date) {
		List<OneSport> oneSports = null;

		try {
			/*
			 * �����ѯ������
			 */
			QueryBuilder<OneSport, Integer> queryBuilder = dtx.getQueryBuilder(
					OneSport.class, Integer.class);
			// ��ѯ����Ϊdate,��count�ε�OneSport
			queryBuilder.where().eq(OneSport.Date_NAME, date).and()
					.eq(OneSport.COUNT_NAME, count);

			// prepare our sql statement
			PreparedQuery<OneSport> query = queryBuilder.prepare();
			oneSports = dtx.query(OneSport.class, Integer.class, query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return oneSports.get(0);
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param SportData
	 * @return
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private List<OneSport> getSportByDate(String SportDate) {

		List<OneSport> oneSports = null;
		try {
			/*
			 * �����ѯ������
			 */
			QueryBuilder<OneSport, Integer> queryBuilder = dtx.getDao(
					OneSport.class, Integer.class).queryBuilder();
			// ��ѯ����Ϊdate������OneSport
			queryBuilder.where().eq(OneSport.Date_NAME, SportDate);
			// prepare our sql statement
			PreparedQuery<OneSport> query = queryBuilder.prepare();
			oneSports = dtx.query(OneSport.class, Integer.class, query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return oneSports;
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean start(Date startTime,User user) {
		// begin-user-code
		// TODO �Զ����ɵķ������

		// ʵ����OneSport
		oneSport = new OneSport();
		// ���ÿ�ʼʱ��
		oneSport.setStartTime(startTime);
		String curDate = DateService.getStringDate();
		oneSport.setDate(curDate);

		// ȥ���ݿ�ȶԿ�ʼʱ��
		int maxNum = dataCollectDataService.getMaxSportNum(startTime);
		oneSport.setCount(maxNum + 1);

		// �������ʲɼ�����
		heartRateService.start();
		// �����ٶȲɼ�����
		speedService.start(user);

		return true;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void stop(Date endTime) {
		// begin-user-code
		// TODO �Զ����ɵķ������

		// ���ý���ʱ��
		oneSport.setEndTime(endTime);
		// ֹͣ���ʲɼ�����,�����ʷ����ȡ��������
		List<HeartRateData> tempHeartRateList = heartRateService.stop();
		// ֹͣ�ٶȲɼ�����
		speedService.stop();

		// ���ٶȷ����ȡ�ٶ�����
		List<SpeedData> tempSpeedList = speedService.getSpeedDataList();

		// ÿ������������
		List<MinuteSportData> minuteSportDataList = new ArrayList<MinuteSportData>();

		for (int i = 0; i < tempHeartRateList.size(); i++) {
			MinuteSportData minuteSportData = new MinuteSportData();

			// װ�䵽minuteSportData
			minuteSportData.setHeartRate(tempHeartRateList.get(i)
					.getHeartRate());// װ������
			minuteSportData.setSpeed(tempSpeedList.get(i).getSpeed());// װ���ٶ�
			minuteSportData.setNumber(i + 1);// װ��ɼ�����
			minuteSportData.setCollectTime(tempHeartRateList.get(i)
					.getCollectTime());// װ��ɼ�ʱ��
			minuteSportData.setOneSport(oneSport);

			// ��ӵ�ÿ������������
			minuteSportDataList.add(minuteSportData);
		}
		/** ��oneSport�����ж�Ӧһ��minuteSportData�Ŵ������ݿ�,���򲻴��� */
		if (minuteSportDataList.size() != 0) {
			oneSport.setlMinuteSportDatas(minuteSportDataList);
			// һ���Ա��浽���ݿ�
			dataCollectDataService.saveSportData(oneSport);
			
//			//��̨�洢
//			String loginFlag = null;
//			try {
//				loginFlag = TxtFileUtil.readTxtFile(TxtFileUtil.loginFlag);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			String[] str = loginFlag.split(",");
//			foundation.webservice.help.OneSport fwho = new foundation.webservice.help.OneSport();
//			foundation.webservice.help.User u = new foundation.webservice.help.User();
//			u.setEmail(str[1]);
//			fwho.setStartTime(oneSport.getStartTime());
//			fwho.setEndTime(oneSport.getEndTime());
//			fwho.setDate(oneSport.getDate());
//			fwho.setCount(oneSport.getCount());
//			fwho.setUser(u);
//			List<foundation.webservice.help.MinuteSportData> fwhms = new ArrayList<foundation.webservice.help.MinuteSportData>();
//			for(MinuteSportData n:minuteSportDataList){
//				foundation.webservice.help.MinuteSportData fwhm = new foundation.webservice.help.MinuteSportData();
//				fwhm.setCollectTime(n.getCollectTime());
//				fwhm.setNumber(n.getNumber());
//				fwhm.setHeartRate(n.getHeartRate());
//				fwhm.setSpeed(n.getSpeed());
//			}
//			webService = new WebServiceUtils(MemoWebPara.NAMESPACE,
//					MemoWebPara.USER_URL, this);
//			HashMap<String, Object> args = new HashMap<String, Object>();
//				
//			args.put("onesport", fwho);
//			webService.callWebService("addOneSport", args, boolean.class);
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * �õ������˶�����
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	// public void getCurrentSportData(Date startTime,Date endTime) {
	// // begin-user-code
	// //ʵ����currentSportData
	// currentSportData=new CurrentSportData();
	//
	// //�ص�UI���onTimeOut
	//
	// dataCollectTimeOut.DOnTimeOut(currentSportData);
	// // end-user-code
	// }

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param weight
	 * @param height
	 * @param distance
	 * @param speed
	 * @return
	 * @generated 
	 *            "UML �� Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double calCalorie(double weight, double speed, double preCalorie) {
		// begin-user-code
		DecimalFormat df1 = new DecimalFormat("0.00");
		double temp = Double.valueOf(df1.format(preCalorie + speed * weight
				/ 400));
		return temp;

		// end-user-code
	}

	@Override
	public void SOnTimeOut(SpeedData speedData) {
		// TODO ʵ���ٶȷ�����onTimeOut����
		// д����־
		Log.w("�����ٶ�", speedData.getSpeed()+"");
		currentSportData.setCurrentSpeed(speedData.getSpeed());// װ�䵱ǰ�ٶ�
		tempDistance += speedData.getDistance();// �ܾ���
		DecimalFormat df1 = new DecimalFormat("0.00");
		double temp = Double.valueOf(df1.format(tempDistance));
		currentSportData.setDistance(temp);// �����������ٶȵ�λΪm/s����Ϊƽ���ٶ�
		// ���㿨·��
		//oneSport.setUser(new User(60));// ����ʹ�ã�����60KG
//		currentSportData.setTotalCalorie(calCalorie(oneSport.getUser()
//				.getWeight(), currentSportData.getCurrentSpeed(),
//				tempTotalCalorie));// ���ÿ�·��ֵ
		currentSportData.setTotalCalorie(speedData.getCalorie());// ���ÿ�·��ֵ
		// ���getCurrentHeartRate!=-1˵��װ�����ʳɹ�,˭������˭���ϲ��onTimeOut
		if (currentSportData.getCurrentHeartRate() != -1) {
			dataCollectTimeOut.DOnTimeOut(currentSportData
					.CopyCurrentSportData(currentSportData));// �ص��ϲ����

			// ���currentSportData������
			currentSportData.ClearCurrentSportData(currentSportData);
		}
	}

	// ʵ�����ʷ�����onTimeOut����
	@Override
	public void HOnTimeOut(HeartRateData heartRateData) {
		Log.w("��������", heartRateData.getHeartRate()+"");
		currentSportData.setCurrentHeartRate(heartRateData.getHeartRate());// װ�䵱ǰ����
		TxtFileUtil
				.appendToFile(
						"HOnTimeOut:" + currentSportData.getCurrentHeartRate()
								+ "\r\n", fh);

		// ˭������˭���ϲ��onTimeOut
		if (currentSportData.getTotalCalorie() != -1) {
			TxtFileUtil.writeSpeed("DaSe - �ٶ���װ��\n");
			dataCollectTimeOut.DOnTimeOut(currentSportData
					.CopyCurrentSportData(currentSportData));// �������ݻص����ϲ����

			// ���currentSportData������
			currentSportData.ClearCurrentSportData(currentSportData);
		}

	}



}
