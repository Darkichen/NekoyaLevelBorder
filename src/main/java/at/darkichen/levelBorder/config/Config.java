package at.darkichen.levelBorder.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public class Config {

    @Getter @Setter
    private boolean isActive;
    @Getter @Setter
    private String prefix;
    @Getter @Setter
    private boolean keepLevelOnDeath;
    @Getter @Setter
    private int expandOnLvlUp;
    @Getter @Setter
    private int defaultRadius;
    @Getter @Setter
    private List<List<Integer>> stage;
    @Getter @Setter
    private int syncLevel;
    @Getter @Setter
    private int syncAmount;
    @Getter @Setter
    private Map<String, Location> border;

    @Override
    public String toString() {
        return "Panel{" +
                "isActive=" + isActive +
                ", prefix='" + prefix + "'" +
                ", keepLevelOnDeath=" + keepLevelOnDeath +
                ", expandOnLvlUp=" + expandOnLvlUp +
                ", defaultRadius=" + defaultRadius +
                ", stage=" + stage +
                ", syncLevel=" + syncLevel +
                ", syncExp=" + syncAmount +
                ", border=" + border +
                "}";
    }
}