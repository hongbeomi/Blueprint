package github.hongbeomi.library

import android.view.View
import android.view.ViewGroup


class ViewFinder {

    private fun findChildByRootToPushInspectorView(view: View, inspectorView: InspectorView) {
        if (view is ViewGroup) {
            pushByViewGroup(view, inspectorView, 0)
        } else {
            inspectorView.offerViewInQueue(view, 0)
        }
        inspectorView.moveFromQueueToStack()
    }

    private fun pushByViewGroup(viewGroup: ViewGroup, inspectorView: InspectorView, level: Int) {
        inspectorView.offerViewInQueue(viewGroup, level)
        val count: Int = viewGroup.childCount
        for (i in 0 until count) {

            val childView = viewGroup.getChildAt(i)
            if (childView is ViewGroup) {
                pushByViewGroup(childView, inspectorView, level + 1)
            } else {
                inspectorView.offerViewInQueue(childView, level + 1)
            }
        }
    }

    fun find(root: ViewGroup, inspectorView: InspectorView) {
        findChildByRootToPushInspectorView(root, inspectorView)
    }

}