<script lang="ts">
    import {createEventDispatcher} from "svelte";
    import {LoggerModel} from "../service";
    import LoggerLevels from "./LoggerLevels.svelte";
    import {queryStore} from "../stores";

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
    export let hide = false;
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
      margin: 0 20px 30px 20px;
    }

    .info {
      display: grid;
      grid-template-columns: auto 335px;
      column-gap: 10px;

      &:hover {
        background-color: #f6f6f6;
        cursor: pointer;
      }

      .info-container {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        padding-top: 5px;

        .name {
          .highlight {
            background-color: lighten(#fbc687, 10%);
          }

          &.toplevel {
            font-size: 1rem;
            font-weight: bold;
          }
        }

        .hide-status {
          font-size: 1.2rem;
          color: #95a5a6;

          &.hide {
            opacity: 0;
          }
        }
      }
    }
  }
</style>

<div class="logger" class:main={depth == 0}>
    <div class="info" style="margin-left: {depth * 15}px">
        <div class="info-container" on:click={() =>  hide = !hide}>
                <span class="hide-status" class:hide={logger.children.length == 0}>
                    {#if hide}+{:else}−{/if}
                </span>
            <span class="name" class:toplevel="{depth == 0}" title="{logger.name}">
              {#each nameparts as part (part.value)}
                <span class:highlight={part.highlight}>{part.value}</span>
              {/each}
            </span>
        </div>
        <div class="levels">
            <LoggerLevels
                    on:update-log-level={(e) => dispatch('update-log-level', new UpdateLogLevelEvent(logger, e.detail.level))}
                    level={logger.level}/>
        </div>
    </div>

    {#if !hide}

        <div class="children">
            {#each logger.children as child (child.name)}
                <svelte:self on:update-log-level logger={child} depth={depth + 1} hide={hide}/>
            {/each}
        </div>
    {/if}
</div>
