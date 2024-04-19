package com.felipe.screenmatch;

import com.felipe.screenmatch.models.TvShow;
import com.felipe.screenmatch.service.ConsumeAPI;
import com.felipe.screenmatch.service.DataConversion;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumeAPI = new ConsumeAPI();
		var json = consumeAPI.getData(
				"https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c"
		);

		DataConversion convert = new DataConversion();
		TvShow data = convert.getData(json, TvShow.class);

		System.out.println(data);
	}
}
