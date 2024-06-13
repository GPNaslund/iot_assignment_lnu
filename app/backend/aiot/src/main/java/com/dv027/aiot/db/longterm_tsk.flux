option task = {
    name: "aggregate_sensor_data",
    every: 1h,
    offset: 10m
}

from(bucket: "realtime")
  |> range(start: -task.every)
  |> filter(fn: (r) => r._measurement == "dht22")
  |> aggregateWindow(every: 1h, fn: mean, createEmpty: false)
  |> to(bucket: "longterm")
 