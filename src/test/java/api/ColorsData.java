package api;

import lombok.Getter;

public class ColorsData {
    @Getter private Integer id;
    @Getter private String name;
    @Getter private Integer year;
    @Getter private String color;
    @Getter private String pantone_value;

    public ColorsData() {}

    public ColorsData(int id, String name, Integer year, String color, String pantone_value) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.color = color;
        this.pantone_value = pantone_value;
    }
}
