package com.ifood.project.router;

import java.util.List;

import com.ifood.project.businesslogic.RegionalClimateFeelingPlaylist;
import com.ifood.project.entities.WorldwidePlaylistResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouterController {

    @Autowired
    private RegionalClimateFeelingPlaylist regionalClimateFeelingPlaylist;

    @RequestMapping(value = "/worldwidePlaylist",method = RequestMethod.GET, params = { "city" })
    @ResponseBody
    public WorldwidePlaylistResult respondUser(@RequestParam(name = "city", required = true) String city) {
        List<String> musics = regionalClimateFeelingPlaylist.getPlaylistByCityName(city);
        return new WorldwidePlaylistResult(musics);
    }

    @RequestMapping(value = "/worldwidePlaylist", method = RequestMethod.GET, params = { "lat", "lon" })
    @ResponseBody
    public WorldwidePlaylistResult respondUser(@RequestParam(name = "lat", required = true) String lat,
            @RequestParam(name = "lon", required = true) String lon) {
        List<String> musics = regionalClimateFeelingPlaylist.getPlaylistByCityCoordinates(lat, lon);
        return new WorldwidePlaylistResult(musics);
    }
}