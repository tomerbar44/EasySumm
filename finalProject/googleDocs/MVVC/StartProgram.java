

package shenkar.finalProject.googleDocs.MVVC;

import javax.swing.*;

// from service we can execute all methods for all google docs documents to do, eg: read, write, delete..
public class StartProgram {

    public static void main(String[] args) {

        try{

            // start from here..
            IViewModel logic = new ViewModel();
            MyLogger.Log.trace("IViewModel obj created");
            IModel model = new Model();
            MyLogger.Log.trace("IModel obj created");
            IView view = new View();
            MyLogger.Log.trace("IView obj created");

            logic.setModel(model);
            logic.setView(view);
            model.setIViewModel(logic);
            view.setIViewModel(logic);
            TestModel t = new TestModel();
            t.addition();


            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        view.start();
                    }
                    catch(MVVMException e ) {
                        MyLogger.Log.error(e.getMessage());
                    }
                }
            });
        }
        catch (Exception ex) {
            System.out.println(" MAIN EXCEPTION! " + ex.getMessage());
            MyLogger.Log.error("fail test!@");
        }


    }

}
