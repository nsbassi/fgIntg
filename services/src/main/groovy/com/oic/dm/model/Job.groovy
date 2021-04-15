package com.oic.dm.model

class Job {
    BigInteger id, logId, essJobId
    String type, status, label, option, parentId, instance, supportsNet, supportsTarget,
            filePrefix, group, icon, runBy, lastRunDate

    boolean running, hasLogs

    List<String> getOptions() {
        def options = []
        if (supportsNet == 'Y') options << 'Net change'
        if (supportsTarget == 'Y') options << 'Target'
        options
    }
}