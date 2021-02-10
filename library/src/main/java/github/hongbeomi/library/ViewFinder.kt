package github.hongbeomi.library

import android.util.Log
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

    private fun pushByViewGroup(viewGroup: ViewGroup, inspectorView: InspectorView, level: Int, intent: String = "") {
        inspectorView.offerViewInQueue(viewGroup, level)
        val count: Int = viewGroup.childCount
        for (i in 0 until count) {

            val childView = viewGroup.getChildAt(i)
            val childString: String =
                childView::class.java.simpleName.toString() + " " + childView.id
            val format =
                "|— %0" +
                        (viewGroup.childCount.toString() + "").length +
                        "d/%0" +
                        (viewGroup.childCount.toString() + "").length +
                        "d %s"
            Log.d(
                "debug",
                intent + java.lang.String.format(
                    format,
                    i + 1,
                    viewGroup.childCount,
                    childString
                )
            )

            if (childView is ViewGroup) {
                pushByViewGroup(childView, inspectorView, level + 1, "$intent  ")
            } else {
                inspectorView.offerViewInQueue(childView, level + 1)
            }
        }
    }

    fun find(root: ViewGroup, inspectorView: InspectorView) {
        findChildByRootToPushInspectorView(root, inspectorView)
    }

}