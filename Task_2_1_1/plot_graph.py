import matplotlib.pyplot as plt
import pandas as pd
import sys
import os

def plot_results(csv_file, output_image):
    if not os.path.exists(csv_file):
        print(f"File {csv_file} not found.")
        return

    try:
        df = pd.read_csv(csv_file)

        # Filter data
        sequential_time = df[df['Method'] == 'Sequential']['TimeMs'].values[0]
        parallel_stream_time = df[df['Method'] == 'ParallelStream']['TimeMs'].values[0]
        parallel_data = df[df['Method'] == 'Parallel']

        threads = parallel_data['NumThreads'].values
        times = parallel_data['TimeMs'].values

        plt.figure(figsize=(10, 6))

        # Plot Parallel (Threads)
        plt.plot(threads, times, marker='o', label='Parallel (Threads)')

        # Plot Sequential (as a horizontal line or single point at x=1)
        plt.axhline(y=sequential_time, color='r', linestyle='--', label=f'Sequential ({sequential_time:.2f} ms)')

        # Plot ParallelStream (as a horizontal line)
        plt.axhline(y=parallel_stream_time, color='g', linestyle='-.', label=f'ParallelStream ({parallel_stream_time:.2f} ms)')

        plt.title(f'Performance Analysis: {csv_file}')
        plt.xlabel('Number of Threads')
        plt.ylabel('Time (ms)')
        plt.legend()
        plt.grid(True)

        plt.savefig(output_image)
        print(f"Graph saved to {output_image}")
        plt.close()

    except Exception as e:
        print(f"Error plotting {csv_file}: {e}")

if __name__ == "__main__":
    plot_results('test3_results.csv', 'performance_graph.png')

