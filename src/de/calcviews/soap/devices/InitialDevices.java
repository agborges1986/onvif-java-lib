package de.calcviews.soap.devices;

import java.net.ConnectException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.onvif.ver10.device.wsdl.GetCapabilities;
import org.onvif.ver10.device.wsdl.GetCapabilitiesResponse;
import org.onvif.ver10.device.wsdl.GetDeviceInformation;
import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;
import org.onvif.ver10.device.wsdl.GetHostname;
import org.onvif.ver10.device.wsdl.GetHostnameResponse;
import org.onvif.ver10.device.wsdl.GetSystemDateAndTime;
import org.onvif.ver10.device.wsdl.GetSystemDateAndTimeResponse;
import org.onvif.ver10.device.wsdl.GetUsers;
import org.onvif.ver10.device.wsdl.GetUsersResponse;
import org.onvif.ver10.device.wsdl.SetHostname;
import org.onvif.ver10.device.wsdl.SetHostnameResponse;
import org.onvif.ver10.device.wsdl.SystemReboot;
import org.onvif.ver10.device.wsdl.SystemRebootResponse;
import org.onvif.ver10.media.wsdl.GetProfiles;
import org.onvif.ver10.media.wsdl.GetProfilesResponse;
import org.onvif.ver10.schema.Capabilities;
import org.onvif.ver10.schema.Date;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.Time;
import org.onvif.ver10.schema.User;

import de.calcviews.soap.OnvifDevice;
import de.calcviews.soap.SOAP;

public class InitialDevices {

	private SOAP soap;
	private OnvifDevice onvifDevice;

	public InitialDevices(OnvifDevice onvifDevice) {
		this.onvifDevice = onvifDevice;
		this.soap = onvifDevice.getSoap();
	}

	public java.util.Date getDate() {
		Calendar cal = null;

		GetSystemDateAndTimeResponse response = new GetSystemDateAndTimeResponse();

		try {
			response = (GetSystemDateAndTimeResponse) soap.createSOAPDeviceRequest(new GetSystemDateAndTime(), response, false);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		Date date = response.getSystemDateAndTime().getUTCDateTime().getDate();
		Time time = response.getSystemDateAndTime().getUTCDateTime().getTime();
		cal = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay(), time.getHour(), time.getMinute(), time.getSecond());

		return cal.getTime();
	}

	public GetDeviceInformationResponse getDeviceInformation() {
		GetDeviceInformation getHostname = new GetDeviceInformation();
		GetDeviceInformationResponse response = new GetDeviceInformationResponse();
		try {
			response = (GetDeviceInformationResponse) soap.createSOAPDeviceRequest(getHostname, response, false);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		return response;
	}

	public String getHostname() {
		GetHostname getHostname = new GetHostname();
		GetHostnameResponse response = new GetHostnameResponse();
		try {
			response = (GetHostnameResponse) soap.createSOAPDeviceRequest(getHostname, response, true);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		return response.getHostnameInformation().getName();
	}

	public boolean setHostname(String hostname) {
		SetHostname setHostname = new SetHostname();
		setHostname.setName(hostname);
		SetHostnameResponse response = new SetHostnameResponse();
		try {
			response = (SetHostnameResponse) soap.createSOAPDeviceRequest(setHostname, response, true);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public List<User> getUsers() {
		GetUsers getUsers = new GetUsers();
		GetUsersResponse response = new GetUsersResponse();
		try {
			response = (GetUsersResponse) soap.createSOAPDeviceRequest(getUsers, response, true);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getUser();
	}

	public Capabilities getCapabilities() throws ConnectException, SOAPException {
		GetCapabilities getCapabilities = new GetCapabilities();
		GetCapabilitiesResponse response = new GetCapabilitiesResponse();

		try {
			response = (GetCapabilitiesResponse) soap.createSOAPRequest(getCapabilities, response, onvifDevice.getDeviceUri(), false);
		}
		catch (SOAPException e) {
			throw e;
		}

		if (response == null) {
			return null;
		}

		return response.getCapabilities();
	}

	public List<Profile> getProfiles() {
		GetProfiles request = new GetProfiles();
		GetProfilesResponse response = new GetProfilesResponse();

		try {
			response = (GetProfilesResponse) soap.createSOAPMediaRequest(request, response, false);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getProfiles();
	}

	public String reboot() {
		SystemReboot request = new SystemReboot();
		SystemRebootResponse response = new SystemRebootResponse();

		try {
			response = (SystemRebootResponse) soap.createSOAPMediaRequest(request, response, true);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getMessage();
	}
}
