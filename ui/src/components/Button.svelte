<script lang="ts">
  import { createEventDispatcher } from "svelte";

  export let label: string;
  export let highlight: boolean;

  const dispatch = createEventDispatcher();
</script>

<style lang="scss">
  button {
    box-shadow: none;
    border-radius: 5px;
    background-color: #ffffff00;
    border: none;
    padding: 5px 10px;
    margin-left: 5px;

    &:hover {
      cursor: pointer;
    }

    &.highlight,
    &:hover {
      &.level-OFF {
        $color: #dddddd;
        background-color: $color;
      }
      &.level-TRACE {
        $color: #cdd4db;
        background-color: $color;
      }
      &.level-DEBUG {
        $color: #a2b4c3;
        background-color: $color;
      }
      &.level-INFO {
        $color: #a4d4ae;
        background-color: $color;
      }
      &.level-WARN {
        $color: #fbc687;
        background-color: $color;
      }
      &.level-ERROR {
        $color: #ea907a;
        background-color: $color;
      }
    }
  }
</style>

<button on:click={() => dispatch("click")} class="level-{label}" type="button" class:highlight>{label}</button>
