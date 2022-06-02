package bong.lines.basic.comm;

@FunctionalInterface
public interface WebHandlerInf<ParamT> {
    void run(ParamT paramT);
}
