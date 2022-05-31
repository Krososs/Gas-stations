package pl.sk.station;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sk.comment.Comment;
import pl.sk.comment.CommentRepository;
import pl.sk.grade.Grade;
import pl.sk.grade.GradeRepository;
import pl.sk.user.UserService;

import java.util.List;

import static pl.sk.support.ResponseUtil.COMMENT_TOO_LONG;
import static pl.sk.support.ResponseUtil.STATION_DOES_NOT_EXISTS;

@RestController
public class StationController {
    private final StationService stationService;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final GradeRepository gradeRepository;

    @Autowired
    public StationController(StationService stationService,UserService userService, CommentRepository commentRepository, GradeRepository gradeRepository) {
        this.stationService = stationService;
        this.userService=userService;
        this.commentRepository = commentRepository;
        this.gradeRepository = gradeRepository;
    }

    @GetMapping("stations/all")
    public ResponseEntity<?> getAllStations(){
        List<Object> stations =  new JSONArray(stationService.getAllStations()).toList();
        return ResponseEntity.ok(stations);
    }

    @PostMapping("stations/new")
    public ResponseEntity<?> addNewStation(@ModelAttribute Station station){

        return ResponseEntity.ok(stationService.addNewStation(station).toJson().toMap());
    }

    @PostMapping("stations/fuel/edit")
    public ResponseEntity<?> editFuelPrice(@ModelAttribute Station station){

        stationService.editFuelPrice(station);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("stations/comment/add")
    public ResponseEntity<?> addComment(@ModelAttribute Comment comment){

        if(!stationService.checkIfStationExists(comment.getStationId())){
            JSONObject error = new JSONObject();
            error.put("error", STATION_DOES_NOT_EXISTS.ToString());
            return new ResponseEntity<>(error.toMap(), HttpStatus.CONFLICT);
        }

        if(comment.getText().length()>150){
            JSONObject error = new JSONObject();
            error.put("error", COMMENT_TOO_LONG.ToString());
            return new ResponseEntity<>(error.toMap(), HttpStatus.CONFLICT);
        }

        commentRepository.save(comment);
        return ResponseEntity.ok("ok");

    }

    @GetMapping("stations/comment/get")
    public ResponseEntity<?> getComments(@RequestParam(name="id") String stationId){
        JSONObject error = new JSONObject();

        if(!stationService.checkIfStationExists(Long.valueOf(stationId))){
            error.put("error", STATION_DOES_NOT_EXISTS.ToString());
            return new ResponseEntity<>(error.toMap(), HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(commentRepository.findBystationId(Long.valueOf(stationId)).toArray());
    }

    @PostMapping("stations/grade/add")
    public ResponseEntity<?> gradeStation(@ModelAttribute Grade grade, @RequestHeader("authorization") String token){

        grade.setUserId(userService.getUserId(token));

        if(gradeRepository.checkIfAssessed(grade.getUserId(), grade.getStationId()).isPresent())
            grade.setId(gradeRepository.checkIfAssessed(grade.getUserId(), grade.getStationId()).get().getId());

        gradeRepository.save(grade);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("stations/details")
    public ResponseEntity<?> getDetails(@RequestParam(name="id") String stationId,@RequestHeader("token") String token){
        JSONObject error = new JSONObject();

        if(!stationService.checkIfStationExists(Long.valueOf(stationId))){
            error.put("error", STATION_DOES_NOT_EXISTS.ToString());
            return new ResponseEntity<>(error.toMap(), HttpStatus.CONFLICT);
        }

        List<Grade> grades =  gradeRepository.findByStationId(Long.valueOf(stationId));

        double gradeSum=0.0;
        double averageGrade=0.0;

        if(grades.size()>0){
            for(Grade g : grades){
                gradeSum+=g.getGrade();
            }
            averageGrade = gradeSum/grades.size();
        }

        JSONObject details =  new JSONObject(stationService.getStationDetails(Long.valueOf(stationId)));

        details.put("average_grade", Math.round(averageGrade*100.0)/100.0);
        details.put("comments_count", commentRepository.countBystationId(Long.valueOf(stationId)));

        if(token.length()<5)
            details.put("user_grade", JSONObject.NULL);
        else if(gradeRepository.checkIfAssessed(userService.getUserId(token),Long.valueOf(stationId)).isPresent())
            details.put("user_grade", gradeRepository.checkIfAssessed(userService.getUserId(token),Long.valueOf(stationId)).get().getGrade());
        else
            details.put("user_grade", JSONObject.NULL);

        return ResponseEntity.ok(details.toMap());

    }

}
