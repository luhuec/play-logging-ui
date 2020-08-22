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

function filterLogger(logger: LoggerModel, query: string): boolean {
    if (logger.name.includes(query)) {
        return true;
    } else {
        return logger.children.filter(child => filterLogger(child, query)).length > 0;
    }
}

export function filterLoggers(loggers: Array<LoggerModel>, query: string): Array<LoggerModel> {
    return loggers.filter(logger => {
        return filterLogger(logger, query)
    });
}
