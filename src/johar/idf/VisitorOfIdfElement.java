package johar.idf;

// Visitor pattern.
public class VisitorOfIdfElement {

    // beforeChildren: called before children are visited.
    // May be overridden by subclasses.
    public void beforeChildren(Idf e, ErrorHandler eh) {
    }
    public void beforeChildren(IdfCommand e, ErrorHandler eh) {
    }
    public void beforeChildren(IdfCommandGroup e, ErrorHandler eh) {
    }
    public void beforeChildren(IdfParameter e, ErrorHandler eh) {
    }
    public void beforeChildren(IdfQuestion e, ErrorHandler eh) {
    }
    public void beforeChildren(IdfStage e, ErrorHandler eh) {
    }
    public void beforeChildren(IdfTable e, ErrorHandler eh) {
    }

    // afterChildren: called after children are visited.
    // May be overridden by subclasses.
    public void afterChildren(Idf e, ErrorHandler eh) {
    }
    public void afterChildren(IdfCommand e, ErrorHandler eh) {
    }
    public void afterChildren(IdfCommandGroup e, ErrorHandler eh) {
    }
    public void afterChildren(IdfParameter e, ErrorHandler eh) {
    }
    public void afterChildren(IdfQuestion e, ErrorHandler eh) {
    }
    public void afterChildren(IdfStage e, ErrorHandler eh) {
    }
    public void afterChildren(IdfTable e, ErrorHandler eh) {
    }

}

