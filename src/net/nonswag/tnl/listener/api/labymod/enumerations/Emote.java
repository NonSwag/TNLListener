package net.nonswag.tnl.listener.api.labymod.enumerations;

import javax.annotation.Nullable;

public enum Emote {
    STOP(-1, null),
    BACKFLIP(2, "Backflip"),
    DAB(3, "Dab"),
    HELLO(4, "Hello"),
    BOW_THANKS(5, "Bow thanks"),
    HYPE(6, "Hype"),
    TRYINGTOFLY(7, "Tryingtofly"),
    INFINITY_SIT(8, "Infinity sit"),
    ZOMBIE(11, "Zombie"),
    HULA_HOOP(13, "Hula Hoop"),
    CALLING(14, "Calling"),
    FACEPALM(15, "Facepalm"),
    BRUSH_YOUR_SHOULDERS(18, "Brush your shoulders"),
    SPLIT(19, "Split"),
    SALUTE(20, "Salute"),
    BALARING(22, "Balaring"),
    HANDSTAND(31, "Handstand"),
    HELICOPTER(32, "Helicopter"),
    HOLY(33, "Holy"),
    WAVEOVER(34, "Waveover"),
    DEEPER_DEEPER(36, "Deeper deeper"),
    KARATE(37, "Karate"),
    MOONWALK(38, "Moonwalk"),
    FREEZING(40, "Freezing"),
    JUBILATION(41, "Jubilation"),
    TURTLE(43, "Turtle"),
    HEADSPIN(45, "Headspin"),
    INFINITY_DAB(46, "Infinity Dab"),
    CHICKEN(47, "Chicken"),
    THE_FLOSS(49, "The Floss"),
    THE_MEGA_THRUST(50, "The mega thrust"),
    THE_CLEANER(51, "The cleaner"),
    BRIDGE(52, "Bridge"),
    MILK_THE_COW(53, "Milk the cow"),
    RURIK(54, "Rurik"),
    WAVE(55, "Wave"),
    MONEY_RAIN(57, "Money rain"),
    THE_POINTER(59, "The pointer"),
    FRIGHTENING(60, "Frightening"),
    SAD(61, "Sad"),
    AIR_GUITAR(62, "Air guitar"),
    WITCH(63, "Witch"),
    LEFT(69, "Left"),
    RIGHT(70, "Right"),
    BUUUH(74, "Buuuh"),
    SPLITTING_BARS(75, "Spitting bars"),
    COUNT_MONEY(76, "Count money"),
    HUG(77, "Hug"),
    APPLAUSE(78, "Applause"),
    BOXING(79, "Boxing"),
    SHOOT(83, "Shoot"),
    THE_POINTING_MAN(84, "The pointing man"),
    HEART(85, "Heart"),
    NEAR_THE_FALL(86, "Near the fall"),
    WAITING(89, "Waiting"),
    PRAISE_YOUR_ITEM(92, "Praise your item"),
    LOOK(93, "Look"),
    I_LOVE_YOU(97, "I love you"),
    SARCASTIC_CLAP(98, "Sarcastic clap"),
    YOU(101, "You"),
    HEAD_ON_WALL(105, "Head on wall"),
    BALANCE(112, "Balance"),
    LEVELUP(113, "Levelup"),
    TAKE_THE_L(114, "Take the L"),
    MY_IDOL(121, "My idol"),
    AIRPLANE(122, "Airplane"),
    EAGLE(124, "Eagle"),
    JOB_WELL_DONE(126, "Job well done"),
    ELEPHANT(128, "Elephant"),
    PRESENT(130, "Present"),
    EYES_ON_YOU(131, "Eyes on you"),
    BOW_DOWN(133, "Bow down"),
    MANEKI_NEKO(134, "Maneki-neko"),
    CONDUCTOR(135, "Conductor"),
    DIDI_CHALLENGE(136, "Didi challenge"),
    SNOW_ANGEL(137, "Snow Angel"),
    SNOWBALL(138, "Snowball"),
    SPRINKLER(139, "Sprinkler"),
    CALCULATED(140, "Calculated"),
    ONE_ARMED_HANDSTAND(141, "One-armed handstand"),
    EAT(142, "Eat"),
    SHY(143, "Shy"),
    SIT_UPS(145, "Sit-Ups"),
    BREAKDANCE(146, "Breakdance"),
    MINDBLOW(148, "Mindblow"),
    FALL(149, "Fall"),
    T_POSE(150, "T Pose"),
    JUMPING_JACK(153, "Jumping Jack"),
    BACKSTROKE(154, "Backstroke"),
    ICE_HOCKEY(156, "Ice-Hockey"),
    LOOK_AT_FIREWORKS(157, "Look at fireworks"),
    FINISH_THE_TREE(158, "Finish the tree"),
    ICE_SKATING(159, "Ice-Skating"),
    FANCY_FEET(161, "Fancy Feet"),
    RONALDO(162, "Ronaldo"),
    TRUE_HEART(163, "True Heart"),
    PUMPERNICKEL(164, "Pumpernickel"),
    BABY_SHARK(166, "Baby Shark"),
    OPEN_PRESENT(167, "Open present"),
    DJ(170, "Dj"),
    HAVE_TO_PEE(171, "Have to pee"),
    SNEEZE(173, "Sneeze"),
    CHEERLEADER(178, "Cheerleader"),
    NARUTO_RUN(180, "Naruto Run"),
    PATI_PATU(181, "Pati Patu"),
    AXE_SWINGING(182, "Axe Swing"),
    FUSION_LEFT(183, "Fusion Left"),
    FISHING(184, "Fishing"),
    FUSION_RIGHT(185, "Fusion Right"),
    BREATHLESS(187, "Breathless"),
    GENKIDAMA(189, "Genkidama"),
    SINGER(191, "Singer"),
    MAGIKARP(192, "Magikarp"),
    RAGE(193, "Rage"),
    SLAP(194, "Slap"),
    AIR_KISSES(195, "Air kisses"),
    KNOCKOUT(196, "Knockout"),
    MATRIX(197, "Matrix"),
    JETPACK(198, "Jetpack"),
    GOLF(200, "Golf"),
    STADIUM_WAVE(201, "Stadium wave"),
    KICKBOXER(202, "Kickboxer"),
    HANDSHAKE(203, "Handshake"),
    CLEANING_THE_FLOOR(204, "Cleaning the floor"),
    ;

    @Nullable private final String name;
    private final int id;

    Emote(int id, @Nullable String name) {
        this.id = id;
        this.name = name;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Emote{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
