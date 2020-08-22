import { fetchLoggers } from "./api";

export class LoggerModel {
    name: string;
    level: string;
    children: Array<LoggerModel>

    constructor(name: string, level: string, children: Array<LoggerModel>) {
        this.name = name;
        this.level = level;
        this.children = children;
    }
}

export function getLoggers(basepath: string): Promise<Array<LoggerModel>> {
    return fetchLoggers(basepath).then(loggerResponse => {
        return loggerResponse.loggers as Array<LoggerModel>;
    })
}


function mapLogger(logger: LoggerModel, query: string): LoggerModel {
    if (logger.name.toLowerCase().includes(query.toLocaleLowerCase())) {
        return logger;
    } else {
        if (logger.children.length > 0) {
            const children = logger.children.map(child => mapLogger(child, query)).filter(c => c != undefined);

            if (children.length == 0) {
                return undefined;
            } else {
                return new LoggerModel(logger.name, logger.level, children);
            }
        } else {
            return undefined;
        }
    }
}

export function filterLoggers(loggers: Array<LoggerModel>, query: string): Array<LoggerModel> {
    return loggers.map(logger => mapLogger(logger, query)).filter(c => c != undefined);
}
