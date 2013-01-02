package org.ortens.bone.core.service;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;


@Tooling(responsability = "Fabrique de Logger")
public class LogsFactory {

    private static Logger logger = null;
    private Handler consoleLog;
    private Handler severeLog;
    private Handler warningLog;
    private Handler infoLog;
    private Handler configLog;
    private Handler finestLog;
    public static final String DEST_DIR = "dest";
    public static final String DEST_SUB_DIR = "rapports";
    public static final String DEST_SUB_DIR2 = "logs";

    public static String destDirLogs() {
        return DEST_DIR + File.separator + DEST_SUB_DIR + File.separator + DEST_SUB_DIR2;
    }

    public Handler creePrePostDomHandler(final Logger logger,
            final String logFile) throws IOException {

        Handler handler = new FileHandler(destDirLogs()
                + File.separator + logFile, false);

        handler.setLevel(Level.FINEST);
        handler.setFormatter(new SimpleFormatter());

        return handler;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        LogsFactory.logger = logger;
    }

    public void initLoggers(final Logger logger) throws IOException {
        this.logger = logger;

        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);

        SimpleFormatter simpleFormatter = new SimpleFormatter();

        consoleLog = new ConsoleHandler();
        consoleLog.setLevel(Level.FINEST);
        consoleLog.setFormatter(simpleFormatter);

        severeLog = new FileHandler(destDirLogs()
                + File.separator + "severeLog.txt", false);
        severeLog.setLevel(Level.SEVERE);
        severeLog.setFormatter(simpleFormatter);

        warningLog = new FileHandler(destDirLogs()
                + File.separator + "warningLog.txt", false);
        warningLog.setLevel(Level.WARNING);
        warningLog.setFormatter(simpleFormatter);

        infoLog = new FileHandler(destDirLogs()
                + File.separator + "infoLog.txt", false);
        infoLog.setLevel(Level.INFO);
        infoLog.setFormatter(simpleFormatter);

        configLog = new FileHandler(destDirLogs()
                + File.separator + "configLog.txt", false);
        configLog.setLevel(Level.CONFIG);
        configLog.setFormatter(simpleFormatter);

        finestLog = new FileHandler(destDirLogs()
                + File.separator + "finestLog.txt", false);
        finestLog.setFormatter(simpleFormatter);
        finestLog.setLevel(Level.FINEST);

        this.logger.addHandler(severeLog);
        this.logger.addHandler(warningLog);
        this.logger.addHandler(infoLog);
        this.logger.addHandler(configLog);
        this.logger.addHandler(finestLog);
        this.logger.addHandler(consoleLog);

    }

    public void flushLogger() {
        consoleLog.flush();
        consoleLog.close();

        severeLog.flush();
        severeLog.close();

        warningLog.flush();
        warningLog.close();

        infoLog.flush();
        infoLog.close();

        configLog.flush();
        configLog.close();

        finestLog.flush();
        finestLog.close();
    }
}
