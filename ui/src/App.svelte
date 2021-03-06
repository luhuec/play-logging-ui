<script lang="ts">
    import {filterLoggers, getLoggers, LoggerModel} from "./service";
    import Logger, {UpdateLogLevelEvent} from "./components/Logger.svelte";
    import {loggerStore, queryStore} from "./stores";
    import {updateLogLevel} from "./api";
    import Header from "./components/Header.svelte";
    import Loader from "./components/Loader.svelte";
    import Error from "./components/Error.svelte";

    export let basepath: string;

    let allLoggers: Array<LoggerModel> = [];
    let loggers: Array<LoggerModel> = [];

    let query = undefined;
    let queryPromise = Promise.resolve();

    const init = () => {
        getLoggers(basepath).then((l) => {
            allLoggers = l;

            if (query.length > 0) {
                loggerStore.set(filterLoggers(l, query));
            } else {
                loggerStore.set(l);
            }
        });
    };

    loggerStore.subscribe((l) => {
        loggers = l;
    });

    queryStore.subscribe((q) => {
        query = q;

        queryPromise = queryPromise.then(() => {

                if (q.length > 0) {
                    loggers = filterLoggers(allLoggers, q);
                } else {
                    loggers = allLoggers;
                }
            }
        )
    });

    const onUpdateLogLevel = (e) => {
        const event: UpdateLogLevelEvent = e.detail;
        updateLogLevel(basepath, event.logger.name, event.level).then((r) =>
            init()
        );
    };

    init();
</script>

<style lang="scss">
  :global(html) {
    overflow-y: scroll;
  }

  :global(body) {
    margin: 0;
    font-family: "Fira Sans", "Arial";
  }
</style>

<div>
    <Error/>
    <Header/>

    {#if loggers.length > 0}
        {#each loggers as logger (logger.name)}
            <Logger on:update-log-level={onUpdateLogLevel} {logger} depth={0}/>
        {/each}
    {:else}
        <Loader />
    {/if}
</div>
