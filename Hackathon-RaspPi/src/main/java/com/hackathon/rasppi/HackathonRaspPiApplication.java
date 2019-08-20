package com.hackathon.rasppi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;

@SpringBootApplication
public class HackathonRaspPiApplication {

	private static String line;
	private static String[] data;
	static int humidity = 0;
	static int temperature = 0;

	public static void main(String[] args) {
		SpringApplication.run(HackathonRaspPiApplication.class, args);

		try {
			testDH11();
		} catch (Exception e) {
			System.out.println("Exception in :: " + e);
		}

	}

	private static void testDH11() throws IOException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("python /home/pi/Downloads/Test/dht.py");
		BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		if ((line = bri.readLine()) != null) {
			if (!(line.contains("ERR_CRC") || line.contains("ERR_RNG"))) {

				data = line.split("ABC");
				System.out.println(data[0]);
				temperature = Integer.parseInt(data[0]);
				humidity = Integer.parseInt(data[1]);
			} else
				System.out.println("Data Error");
		}

		bri.close();
		p.waitFor();
		System.out.println("Temperature is : " + temperature + " 'C Humidity is :" + humidity + " %RH");
		System.out.println("Done.");

	}

	private static void testBasedOnFamily() {
		W1Master master = new W1Master();
		System.out.println(master.getDevices());
		for (W1Device w1device : master.getDevices()) {
			System.out
					.println(w1device.getName() + " :: " + w1device.getFamilyId() + ":: " + w1device.getId() + " :: ");
		}
		List<W1Device> w1Devices = master.getDevices(TmpDS18B20DeviceType.FAMILY_CODE);
		for (W1Device device : w1Devices) {
			// this line is enought if you want to read the temperature
			System.out.println("Temperature: " + ((TemperatureSensor) device).getTemperature());
			// returns the temperature as double rounded to one decimal place after the
			// point

			try {
				System.out.println("1-Wire ID: " + device.getId() + " value: " + device.getValue());
				//
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
