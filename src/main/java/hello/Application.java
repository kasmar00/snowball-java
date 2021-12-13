package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.lang.Math;

@SpringBootApplication
@RestController
public class Application {

  static class Self {
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate {
    public Links _links;
    public Arena arena;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!";
  }

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
    System.out.println(arenaUpdate);

    String[] directions = {"N", "S", "E", "W"};
    String[] rotations = new String[]{"R", "L"};
    String[] rotationsAndForward = new String[]{"R", "L", "F"};
    int i = new Random().nextInt(3);

    PlayerState me = arenaUpdate.arena.state.get(arenaUpdate._links.self.href);
    if (me.wasHit) {
        return rotationsAndForward[i];
    }
    for (Entry<String,PlayerState> entry : arenaUpdate.arena.state.entrySet()) {
        if (entry.getKey().equals(arenaUpdate._links.self.href)){

        } else {
            double dist = calcDistance(entry.getValue(), me);
            String dir = direction(me, entry.getValue());
            if (dist<=3 && dir.equals(me.direction)) {
                return "T";
            } else if (dist <=4 && dir.equals(me.direction)) {
                return "F";
            } else if (dist<=2 && Arrays.asList(directions).contains(dir)) {
                // return rotateTo(me.direction, dir);
                int choice = new Random().nextInt(2);
                return rotations[choice];
            }
        }
    }
    return rotationsAndForward[i];
  }

  public double calcDistance(PlayerState a, PlayerState b) {
    return Math.sqrt(Math.pow(a.x-b.x,2)+Math.pow(a.y-b.y, 2));
  }

  public String direction(PlayerState a, PlayerState b) {
      if (a.x==b.x && a.y > b.y) {
          return "N";
      } else if (a.x==b.x && a.y < b.y) {
          return "S";
      } else if (a.y==b.y && a.x < b.x) {
          return "E";
      } else if (a.y==b.y && a.x > b.x) {
          return "W";
      }
      return "";
  }

//   public String roatetTo(String me, String other){

//   }

}

