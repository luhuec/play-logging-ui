<script lang="ts">
  import { createEventDispatcher } from "svelte";
  import { LoggerModel } from "../service";
  import LoggerLevels from "./LoggerLevels.svelte";
  import { children } from "svelte/internal";
  import { queryStore } from "../stores";

  class NamePart {
    value: string;
    highlight: boolean;

    constructor(value: string, highlight: boolean) {
      this.value = value;
      this.highlight = highlight;
    }
  }

  export class UpdateLogLevelEvent {
    logger: LoggerModel;
    level: string;

    constructor(logger: LoggerModel, level: string) {
      this.logger = logger;
      this.level = level;
    }
  }

  export let depth: number;
  export let logger: LoggerModel;
  let nameparts = [new NamePart(logger.name, false)];

  queryStore.subscribe((q) => {
    if (q.length > 0) {
      const parts = [];

      let p = 0;
      let index = -1;
      while (
        (index = logger.name
          .toLocaleLowerCase()
          .indexOf(q.toLocaleLowerCase(), index + 1)) >= 0
      ) {
        if (p < index) {
          parts.push(new NamePart(logger.name.slice(p, index), false));
        }

        parts.push(
          new NamePart(logger.name.slice(index, index + q.length), true)
        );

        p = index + q.length;
      }

      if (p < logger.name.length) {
        parts.push(
          new NamePart(logger.name.slice(p, logger.name.length), false)
        );
      }

      nameparts = parts;
    } else {
      nameparts = [new NamePart(logger.name, false)];
    }
  });

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

      .name {
        .highlight {
          background-color: lighten(#fbc687, 10%);
        }
      }
    }

    .children {
      .info {
        border-top: 1px solid #e6e6e6;
      }
    }
  }
</style>

<div class="logger" class:main={depth == 0}>
  <div class="info" style="margin-left: {depth * 20}px">
    <span class="name">
      {#each nameparts as part (part.value)}
        <span class:highlight={part.highlight}>{part.value}</span>
      {/each}
    </span>
    <div class="levels">
      <LoggerLevels
        on:update-log-level={(e) => dispatch('update-log-level', new UpdateLogLevelEvent(logger, e.detail.level))}
        level={logger.level} />
    </div>
  </div>

  <div class="children">
    {#each logger.children as child (child.name)}
      <svelte:self on:update-log-level logger={child} depth={depth + 1} />
    {/each}
  </div>
</div>
