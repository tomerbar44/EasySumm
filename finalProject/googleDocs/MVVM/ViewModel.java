package shenkar.finalProject.googleDocs.MVVC;

import com.google.api.services.docs.v1.Docs;

/**
 * will connect between View and Model
 */
public class ViewModel implements IViewModel {
    private IModel model;
    private IView view;


    @Override
    public void setModel(IModel model) throws MVVMException {
        this.model = model;
    }

    @Override
    public void setView(IView view) throws MVVMException {
    this.view = view;
    }


    @Override
    public void createTemplatedDocFromModel(String titleName, String authorName, String summary, String bullets) throws MVVMException {
        new Thread(() -> {
            try{
                Docs currService = model.getDocsService();
                String newDocId = model.createDoc(currService, titleName);
                model.setBodyTemplateText(currService,newDocId,"" +
                        "\t\t\t\t\tToDo - {{Name}}\t\t\t{{date}}\n" +
                        "{{List}}\n\n" +
                        "\t\t\t\t\tSummery\n{{Summery}}"
                );
                model.setDocStyle(currService,newDocId);
                model.replaceTemplatedText(currService,newDocId,authorName,summary, bullets);
                //!!! function that shows something else to client when document complete
                //!!! need to pass the newDocId then.
                view.showSuccessToCreateDoc(newDocId);
            } catch (MVVMException ex) {
                MyLogger.Log.error("Cant create new Doc - " + ex.getMessage() + ex.getCause());
                System.err.println(ex.getMessage() + ex.getCause());
            }

        }).start();

        view.showWaitTillDocCreate();
    }
}
