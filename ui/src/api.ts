import { LoggerModel } from "./service";

export class GetLoggersLogger {
  name: string;
  level: string;
  children: Array<GetLoggersLogger>;

  constructor(name: string, level: string, children: Array<GetLoggersLogger>) {
    this.name = name;
    this.level = level;
    this.children = children;
  }
}

export class GetLoggersResponse {
  loggers: Array<GetLoggersLogger>;

  constructor(loggers: Array<GetLoggersLogger>) {
    this.loggers = loggers;
  }
}

export function fetchLoggers(basepath: string): Promise<GetLoggersResponse> {
  return fetch(basepath + "/playloggingui/logger")
    .then(response => response.json())
    .then(data => {
      const loggers = data as Array<GetLoggersLogger>
      return new GetLoggersResponse(loggers)
    });
}

export function updateLogLevel(basepath: string, logger: string, level: string): Promise<boolean> {
  return fetch(`${basepath}/playloggingui/logger/update?logger=${logger}&level=${level}`).then(r => true);
}

