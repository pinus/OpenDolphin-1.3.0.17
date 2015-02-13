/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.impl.scheam.widget;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

/**
 *
 * @author pns
 */
public class NewClass extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SimpleObjectProperty<Test> prop = new SimpleObjectProperty<>();
        prop.addListener(new ChangeListener<Test>(){
            @Override
            public void changed(ObservableValue<? extends Test> ov, Test t, Test t1) {
                System.out.println("changed!!");
                System.out.println("t = " + t + "  t1 = " + t1);
            }
        });

        Test test1 = new Test();
        Test test2 = new Test();
        prop.set(test1);
        prop.set(test2);
    }

    public static void main (String[] arg) {
        launch(arg);
    }

    class Test {
        @Override
        public boolean equals(Object o) {
            return false;
        }
    }
}
