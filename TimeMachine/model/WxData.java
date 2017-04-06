package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the wx_data database table.
 * 
 */
@Entity
@Table(name="wx_data")
@NamedQuery(name="WxData.findAll", query="SELECT w FROM WxData w")
public class WxData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private double apparentTemperatureMax;

	private int apparentTemperatureMaxTime;

	private double apparentTemperatureMin;

	private int apparentTemperatureMinTime;

	private double cloudCover;

	private double dewPoint;

	private double humidity;

	private String icon;

	private String latitude;

	private String longitude;

	private double moonphase;

	private short offset;

	private double precipIntensity;

	private double precipIntensityMax;

	private double precipIntensityMaxTime;

	private double precipIntensityProbability;

	private String precipType;

	private double pressure;

	private String summary;

	private int sunriseTime;

	private int sunsetTime;

	private double temperatureMax;

	private int temperatureMaxTime;

	private double temperatureMin;

	private int temperatureMinTime;

	private int time;

	private String timezone;

	private double visibility;

	private int windBearing;

	private double windSpeed;

	//bi-directional many-to-one association to WxUser
	@ManyToOne
	@JoinColumn(name="fk_user_id")
	private WxUser wxUser;

	public WxData() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getApparentTemperatureMax() {
		return this.apparentTemperatureMax;
	}

	public void setApparentTemperatureMax(double apparentTemperatureMax) {
		this.apparentTemperatureMax = apparentTemperatureMax;
	}

	public int getApparentTemperatureMaxTime() {
		return this.apparentTemperatureMaxTime;
	}

	public void setApparentTemperatureMaxTime(int apparentTemperatureMaxTime) {
		this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;
	}

	public double getApparentTemperatureMin() {
		return this.apparentTemperatureMin;
	}

	public void setApparentTemperatureMin(double apparentTemperatureMin) {
		this.apparentTemperatureMin = apparentTemperatureMin;
	}

	public int getApparentTemperatureMinTime() {
		return this.apparentTemperatureMinTime;
	}

	public void setApparentTemperatureMinTime(int apparentTemperatureMinTime) {
		this.apparentTemperatureMinTime = apparentTemperatureMinTime;
	}

	public double getCloudCover() {
		return this.cloudCover;
	}

	public void setCloudCover(double cloudCover) {
		this.cloudCover = cloudCover;
	}

	public double getDewPoint() {
		return this.dewPoint;
	}

	public void setDewPoint(double dewPoint) {
		this.dewPoint = dewPoint;
	}

	public double getHumidity() {
		return this.humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public double getMoonphase() {
		return this.moonphase;
	}

	public void setMoonphase(double moonphase) {
		this.moonphase = moonphase;
	}

	public short getOffset() {
		return this.offset;
	}

	public void setOffset(short offset) {
		this.offset = offset;
	}

	public double getPrecipIntensity() {
		return this.precipIntensity;
	}

	public void setPrecipIntensity(double precipIntensity) {
		this.precipIntensity = precipIntensity;
	}

	public double getPrecipIntensityMax() {
		return this.precipIntensityMax;
	}

	public void setPrecipIntensityMax(double precipIntensityMax) {
		this.precipIntensityMax = precipIntensityMax;
	}

	public double getPrecipIntensityMaxTime() {
		return this.precipIntensityMaxTime;
	}

	public void setPrecipIntensityMaxTime(double precipIntensityMaxTime) {
		this.precipIntensityMaxTime = precipIntensityMaxTime;
	}

	public double getPrecipIntensityProbability() {
		return this.precipIntensityProbability;
	}

	public void setPrecipIntensityProbability(double precipIntensityProbability) {
		this.precipIntensityProbability = precipIntensityProbability;
	}

	public String getPrecipType() {
		return this.precipType;
	}

	public void setPrecipType(String precipType) {
		this.precipType = precipType;
	}

	public double getPressure() {
		return this.pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getSunriseTime() {
		return this.sunriseTime;
	}

	public void setSunriseTime(int sunriseTime) {
		this.sunriseTime = sunriseTime;
	}

	public int getSunsetTime() {
		return this.sunsetTime;
	}

	public void setSunsetTime(int sunsetTime) {
		this.sunsetTime = sunsetTime;
	}

	public double getTemperatureMax() {
		return this.temperatureMax;
	}

	public void setTemperatureMax(double temperatureMax) {
		this.temperatureMax = temperatureMax;
	}

	public int getTemperatureMaxTime() {
		return this.temperatureMaxTime;
	}

	public void setTemperatureMaxTime(int temperatureMaxTime) {
		this.temperatureMaxTime = temperatureMaxTime;
	}

	public double getTemperatureMin() {
		return this.temperatureMin;
	}

	public void setTemperatureMin(double temperatureMin) {
		this.temperatureMin = temperatureMin;
	}

	public int getTemperatureMinTime() {
		return this.temperatureMinTime;
	}

	public void setTemperatureMinTime(int temperatureMinTime) {
		this.temperatureMinTime = temperatureMinTime;
	}

	public int getTime() {
		return this.time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getTimezone() {
		return this.timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public double getVisibility() {
		return this.visibility;
	}

	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}

	public int getWindBearing() {
		return this.windBearing;
	}

	public void setWindBearing(int windBearing) {
		this.windBearing = windBearing;
	}

	public double getWindSpeed() {
		return this.windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public WxUser getWxUser() {
		return this.wxUser;
	}

	public void setWxUser(WxUser wxUser) {
		this.wxUser = wxUser;
	}

}