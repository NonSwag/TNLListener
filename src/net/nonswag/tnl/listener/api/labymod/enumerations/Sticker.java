package net.nonswag.tnl.listener.api.labymod.enumerations;

import javax.annotation.Nonnull;

public enum Sticker {
    PUMPKIN(1, "Pumpkin"),
    HAPPY_HALLOWEEN(2, "Happy Halloween"),
    TRICK_OR_TREAT(3, "Trick or treat"),
    BOO(4, "Boo"),
    GHOST(5, "Ghost"),
    SANTA(6, "Santa"),
    REINDEER(7, "Reindeer"),
    MERRY_CHRISTMAS(8, "Merry Christmas"),
    HOHOHO(9, "Hohoho"),
    CHRISTMAS_BALL(10, "Christmas Ball"),
    REVED_DERP(17, "Reved Derp"),
    REVED_HI(18, "Reved Hi"),
    REVED_LOVE(19, "Reved Love"),
    REVED_PATRICK(20, "Reved Patrick"),
    REVED_RIP(21, "Reved Rip"),
    HEART(22, "Heart"),
    LOVE_CHOCOLATE(23, "Love Chocolate"),
    LOVE_GLASSES(24, "Love Glasses"),
    LOVE_TEDDY(25, "Love Teddy"),
    LOVE_RINGS(26, "Love Rings"),
    SOCIAL_DISTANCING(27, "Social Distancing"),
    WASH_YOUR_HANDS(28, "Wash your hands"),
    DONT_TOUCH_YOUR_FACE(29, "Don't touch your face"),
    STAY_AT_HOME(30, "Stay at home"),
    VIRUS(31, "Virus"),
    LABYMOD_LOGO(34, "LabyMod Logo"),
    LABYCOOL(35, "LabyCool"),
    FIVE_YEARS(36, "5 Years"),
    LABYWINGS(37, "LabyWings"),
    LABY_CHEST(38, "Laby Chest"),
    PIKA_SPACE(39, "Pika Space"),
    POG_SPACE(40, "Pog Space"),
    SPACE_HI(41, "Space Hi"),
    SPACE_KRONE(42, "Space Krone"),
    SPACE_LOVE(43, "Space Love"),
    SCHMOCKYYY_SUPERMAN(44, "Schmockyyy Superman"),
    SCHMOCKYYY_LOGO(45, "Schmockyyy Logo"),
    SCHMOCKYYY_GG(46, "Schmockyyy GG"),
    SCHMOCKYYY_YYY(47, "Schmockyyy YYY"),
    SCHMOCKYYY_BANANE(48, "Schmockyyy Banane"),
    FLEXICAP(49, "FlexiCap"),
    FLEXICUTE(50, "FlexiCute"),
    FLEXILOST(51, "FlexiLost"),
    FLEXIUFF(52, "FlexiUff"),
    FLEXIWOW(53, "FlexiWow"),
    LUMPI_LOVE(54, "Lumpi Love"),
    LUMPI_ANGRY(55, "Lumpi Angry"),
    LUMPI_CLAP(56, "Lumpi Clap"),
    LUMPI_FACEPALM(57, "Lumpi Facepalm"),
    LUMPI_SAD(58, "Lumpi Sad"),
    SPIRITENDO_AW(59, "Spiritendo Aw"),
    SPIRITENDO_CRY(60, "Spiritendo Cry"),
    SPIRITENDO_HYPE(61, "Spiritendo Hype"),
    SPIRITENDO_LOVE(62, "Spiritendo Love"),
    SPIRITENDO_SIP(63, "Spiritendo Sip"),
    ;

    private final int id;
    @Nonnull private final String name;

    Sticker(int id, @Nonnull String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Sticker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
