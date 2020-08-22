<script lang="ts">
  import { createEventDispatcher } from "svelte";
  import { LoggerModel } from "../service";
  import LoggerLevels from "./LoggerLevels.svelte";
  import { children } from "svelte/internal";
  import { queryStore } from "../stores";

  export class UpdateLogLevelEvent {
    logger: LoggerModel;
    level: string;

    constructor(logger: LoggerModel, level: string) {
      this.logger = logger;
      this.level = level;
    }
  }

  let query: string = undefined;

  export let depth: number;
  export let logger: LoggerModel;

  if (logger.name == "play" || logger.name == "play.core") {
    console.log(logger)
  }

  queryStore.subscribe((q) => (query = q));

  const dispatch = createEventDispatcher();
</script>

<style lang="scss">
  .logger {
    &.main {
      background-color: #fbfbfb;
      padding: 10px 10px;
      margin: 10px 20px;
      border: 1px solid #ececec;
    }

    .info {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
      padding: 5px 0;

      &:hover {
        background-color: #f6f6f6;
      }
    }

    .children {
      .info {
        border-top: 1px solid #dddddd;
      }
    }
  }
</style>

<div class="logger" class:main={depth == 0}>
  <div class="info" style="margin-left: {depth * 20}px">
    <span class="name">
      {#if query}{logger.name}{:else}{logger.name}{/if}
    </span>
    <div class="levels">
      <LoggerLevels
        on:update-log-level={(e) => dispatch('update-log-level', new UpdateLogLevelEvent(logger, e.detail.level))}
        level={logger.level} />
    </div>
  </div>

  <div class="children">
    {#each logger.children as child}
      <svelte:self on:update-log-level logger={child} depth={depth + 1} />
    {/each}
  </div>
</div>
