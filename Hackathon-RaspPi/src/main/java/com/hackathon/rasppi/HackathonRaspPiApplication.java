package com.hackathon.rasppi;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;

@SpringBootApplication
public class HackathonRaspPiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackathonRaspPiApplication.class, args);
		
		W1Master master = new W1Master();
		System.out.println(master.getDevices());
		for(W1Device w1device : master.getDevices())
		{
			System.out.println(w1device.getName() + " :: "+ w1device.getFamilyId() + ":: " + w1device.getId() + " :: ");
		}
		List<W1Device> w1Devices = master.getDevices(TmpDS18B20DeviceType.FAMILY_CODE);
		for (W1Device device : w1Devices) {
		    //this line is enought if you want to read the temperature
		    System.out.println("Temperature: " + ((TemperatureSensor) device).getTemperature());
		    //returns the temperature as double rounded to one decimal place after the point

		    try {
		        System.out.println("1-Wire ID: " + device.getId() +  " value: " + device.getValue());
		        //
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
	}

}
