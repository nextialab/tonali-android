package com.nextialab.tonali.support;

import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.model.Task;

/**
 * Created by Nelson on 8/13/2015.
 */
public interface ActivityListener {

    void goToList(TonaliList list);
    void goToFinal(TonaliList list);

}
