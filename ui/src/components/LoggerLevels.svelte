<script lang="ts">
  import { createEventDispatcher } from "svelte";
  import Button from "./Button.svelte";

  class LogLevelInfo {
    level: string;

    constructor(level: string) {
      this.level = level;
    }
  }

  const allLevels: Array<LogLevelInfo> = [
    new LogLevelInfo("OFF"),
    new LogLevelInfo("DEBUG"),
    new LogLevelInfo("INFO"),
    new LogLevelInfo("WARN"),
    new LogLevelInfo("ERROR"),
  ];

  export let level: string;

  const dispatch = createEventDispatcher();
</script>

<style lang="scss">

</style>

<div>
  {#each allLevels as logLevel}
    <Button
      on:click={() => dispatch('update-log-level', logLevel)}
      label={logLevel.level}
      highlight={logLevel.level == level} />
  {/each}
</div>
