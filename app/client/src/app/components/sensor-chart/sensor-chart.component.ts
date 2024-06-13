import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartDataset, ChartOptions } from 'chart.js';
import { ISensorData } from '../../model/sensor-data/sensor-data.interface';
import { BaseChartDirective } from 'ng2-charts';

/**
 * Component for displaying sensor data in a chart.
 * Uses Chart.js as a chart engine.
 *
 * @export
 * @class SensorChartComponent
 * @typedef {SensorChartComponent}
 * @implements {OnChanges}
 */
@Component({
  selector: 'app-sensor-chart',
  standalone: true,
  imports: [BaseChartDirective],
  templateUrl: './sensor-chart.component.html',
  styleUrls: ['./sensor-chart.component.css']
})

export class SensorChartComponent implements OnChanges {
  @Input() dataPoints: ISensorData[] = [];

  public lineChartData: ChartDataset[] = [
    { data: [], label: 'Temperature', borderColor: 'rgba(255, 99, 132, 1)', backgroundColor: 'rgba(255, 99, 132, 0.2)' },
    { data: [], label: 'Humidity', borderColor: 'rgba(54, 162, 235, 1)', backgroundColor: 'rgba(54, 162, 235, 0.2)' }
  ];

  public lineChartLabels: string[] = [];

  public lineChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      x: {
        display: false,
        type: 'linear',
        title: {
          display: true,
          text: 'Date'
        },
      },
      y: {
        type: 'linear',
        position: 'left',
        min: 0,
        max: 100,
        ticks: {
          callback: function(tickValue: string | number): string {
            return `${tickValue}°C`;
          }
        },
        title: {
          display: true,
          text: 'Temperature (°C)'
        }
      },
      y1: {
        type: 'linear',
        position: 'right',
        min: 0,
        max: 100,
        ticks: {
          callback: function(tickValue: string | number): string {
            return `${tickValue}%`;
          }
        },
        title: {
          display: true,
          text: 'Humidity (%)'
        }
      }
    },
    plugins: {
      tooltip: {
        callbacks: {
          title: (tooltipItem) => {
            const date = new Date(tooltipItem[0].parsed.x);
            return date.toLocaleString();
          }
        }
      }
    }
  };

  public lineChartLegend = true;
  public lineChartType: any = 'line';

  ngOnChanges(changes: SimpleChanges) {
    if (changes['dataPoints'] && this.dataPoints) {
      this.processData();
    }
  }

  private processData() {
    const temperatureData: { x: number, y: number }[] = [];
    const humidityData: { x: number, y: number }[] = [];
    const labels: string[] = [];

    this.dataPoints.forEach(dataPoint => {
      const timeStamp = dataPoint.time * 1000;
      labels.push(new Date(timeStamp).toLocaleDateString());
      temperatureData.push({ x: timeStamp, y: dataPoint.temperature });
      humidityData.push({ x: timeStamp, y: dataPoint.humidity });
    });

    this.lineChartData = [
      { data: temperatureData, label: 'Temperature', yAxisID: 'y' },
      { data: humidityData, label: 'Humidity', yAxisID: 'y1' }
    ];
    this.lineChartLabels = labels;

    const minTime = Math.min(...temperatureData.map(d => d.x), ...humidityData.map(d => d.x));
    const maxTime = Math.max(...temperatureData.map(d => d.x), ...humidityData.map(d => d.x));

    const xScale = this.lineChartOptions.scales?.['x'] ?? {};
    xScale.min = minTime;
    xScale.max = maxTime;
    this.lineChartOptions.scales = { ...this.lineChartOptions.scales, x: xScale };

  }
}
