package shenkar.finalProject.googleDocs.MVVC;

// the methods should throw MVVMException
public interface IViewModel {
public void setModel(IModel model) throws  MVVMException;
public void setView(IView view) throws  MVVMException;

    /**
     * @param titleName - the file title name
     * @param authorName - the name of the creator from UI
     * @param summary - the summery from UI
     * @param bullets - the bullets from UI
     * @throws MVVMException - customise Exception
     *
     * In a new Thread, we creating the document
     */
public void createTemplatedDocFromModel(String titleName, String authorName, String summary, String bullets) throws MVVMException;

}
