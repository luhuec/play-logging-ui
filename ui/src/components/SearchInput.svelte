<script lang="ts">
  import { queryStore } from "../stores";

  let query: string = "";

  $: queryStore.set(query);
</script>

<style lang="scss">
  input {
    background-color: #ffffff00;
    color: #ffffff;
    border: 2px solid lighten(#2c3e50, 30%);
    border-radius: 5px;
    padding: 10px 10px;

    &::placeholder {
      color: #ffffff;
      opacity: 0.5;
    }
  }
</style>

<div>
  <input bind:value={query} type="text" placeholder="Filter loggers" />
</div>
