package pl.sk.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<?> getAllStations(){
        return stationRepository.findAll().stream().map(station -> station.toJson()).collect(Collectors.toList());
    }

    public Station addNewStation(Station station){
        return stationRepository.save(station);
    }

    public Station getStationDetails(Long stationId){

        return stationRepository.findById(stationId).get();
    }

    public void editFuelPrice(Station station){

        Station nStation = stationRepository.findById(station.getId()).get();

        if(station.getGas()!=null){
            nStation.setGas(station.getGas());
        }
        if(station.getPb95()!=null){
            nStation.setPb95(station.getPb95());
        }

        if(station.getPb98()!=null){
            nStation.setPb98(station.getPb98());
        }
        if(station.getDiesel()!=null){
            nStation.setDiesel(station.getDiesel());
        }

       stationRepository.save(nStation);

    }

    public boolean checkIfStationExists(Long id){
        return stationRepository.findById(id).isPresent();
    }

}
