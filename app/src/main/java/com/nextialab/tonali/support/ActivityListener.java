package com.nextialab.tonali.support;

import com.nextialab.tonali.model.List;
import com.nextialab.tonali.model.Task;

/**
 * Created by Nelson on 8/13/2015.
 */
public interface ActivityListener {

    void goToList(List list);
    void goToTask(Task task);

}
