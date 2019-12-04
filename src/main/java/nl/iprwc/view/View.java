package nl.iprwc.view;

public class View {
    public static class Public{}
    public static class Private extends Public{}
    public static class All extends Private{}

    public static class Simple extends Public{}
    public static class Modeled extends Simple{}

    public static class ProjectFilter {};
}
