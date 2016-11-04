package gui;

import java.awt.Button;
import java.awt.Container;
import java.awt.Label;
import java.awt.TextField;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Window extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7446192599263749847L;
	private static final int N = 20;

	private double dataPoints[] = new double[N];

	private Integer oldreceived;
	private long oldTime;

	private Integer sent = new Integer(0), received = new Integer(0);

	public Window() {
		// initiate variables
		oldTime = System.currentTimeMillis();
		oldreceived = received;
		for (int i = 0; i < N; i++)
			dataPoints[i] = 0;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Container panel = getContentPane();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);

		ImageComponent image = null;
		try {
			image = new ImageComponent(createImage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		}
		Thread imageThread = new Thread(new ImageCreator(image));
		imageThread.setDaemon(true);
		imageThread.start();

		Label sentLabel = new Label("Packets sent");
		TextField sentField = new TextField(15);
		sentField.setEditable(false);
		sentField.setText(sent.toString());

		Label receivedLabel = new Label("Packets received");
		TextField receivedField = new TextField(15);
		receivedField.setEditable(false);
		receivedField.setText(received.toString());

		Button send = new Button("Send Broadcast");
		send.addActionListener(e -> {
			sendBroadcast(3333);
			synchronized (sent) {
				sent++;
			}
			SwingUtilities.invokeLater(() -> {
				synchronized (sent) {
					sentField.setText(sent.toString());
				}
			});
		});

		Button activate = new Button("Activate STP");
		activate.addActionListener(e -> sendBroadcast(1337));

		Button deactivate = new Button("Deactivate STP");
		deactivate.addActionListener(e -> sendBroadcast(1338));

		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(image)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup().addComponent(sentLabel).addComponent(receivedLabel))
						.addGroup(layout.createParallelGroup().addComponent(sentField).addComponent(receivedField)))
				.addComponent(send)
				.addGroup(layout.createSequentialGroup().addComponent(activate).addComponent(deactivate)));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(image)
				.addGroup(layout.createBaselineGroup(true, false).addComponent(sentLabel).addComponent(sentField))
				.addGroup(
						layout.createBaselineGroup(true, false).addComponent(receivedLabel).addComponent(receivedField))
				.addComponent(send)
				.addGroup(layout.createParallelGroup().addComponent(activate).addComponent(deactivate)));

		Thread listenerThread = new Thread(new Listener(receivedField));
		listenerThread.setDaemon(true);
		listenerThread.start();
	}

	private void sendBroadcast(int port) {
		try (DatagramSocket sock = new DatagramSocket()) {
			sock.setBroadcast(true);
			String message = "aoeu";
			DatagramPacket toSend = new DatagramPacket(message.getBytes(), message.getBytes().length);
			toSend.setSocketAddress(new InetSocketAddress("192.168.1.255", port));
			sock.send(toSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] createImage() throws IOException {
		XYSeries series = new XYSeries("Network Load");
		for (int i = 0; i < N; i++)
			series.add(i, dataPoints[i]);
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);

		JFreeChart chart = ChartFactory.createXYLineChart("Network Load", "", "", dataset);

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ChartUtilities.writeChartAsPNG(bOut, chart, 300, 300);
		return bOut.toByteArray();
	}

	private class Listener implements Runnable {
		private TextField receivedField;

		public Listener(TextField receivedField) {
			this.receivedField = receivedField;
		}

		@Override
		public void run() {
			try (DatagramSocket sock = new DatagramSocket(3333)) {
				byte buffer[] = new byte[1024];
				DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);

				while (true) {
					sock.receive(receivedPacket);
					synchronized (received) {
						received++;
					}
					SwingUtilities.invokeLater(() -> {
						synchronized (received) {
							receivedField.setText(received.toString());
						}
					});
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class ImageCreator implements Runnable {
		ImageComponent img;

		public ImageCreator(ImageComponent img) {
			super();
			this.img = img;
		}

		@Override
		public void run() {
			while (true) {
				int deltaR;
				synchronized (received) {
					deltaR = received - oldreceived;
					oldreceived = received;
				}
				long time = System.currentTimeMillis();
				long deltaT = time - oldTime;
				oldTime = time;

				// shift array forward
				for (int i = N - 1; i > 0; i--)
					dataPoints[i - 1] = dataPoints[i];

				dataPoints[N - 1] = (double) deltaR * 1000 / deltaT;

				try {
					img.setImage(createImage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
