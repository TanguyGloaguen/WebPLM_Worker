package test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Command;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.FlowListener;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.Method;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Basic.RecoverOk;
import com.rabbitmq.client.AMQP.Confirm.SelectOk;
import com.rabbitmq.client.AMQP.Exchange.BindOk;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;
import com.rabbitmq.client.AMQP.Exchange.DeleteOk;
import com.rabbitmq.client.AMQP.Exchange.UnbindOk;
import com.rabbitmq.client.AMQP.Queue.PurgeOk;
import com.rabbitmq.client.AMQP.Tx.CommitOk;
import com.rabbitmq.client.AMQP.Tx.RollbackOk;

import server.Connector;
import server.Main;

public class ConnectorTest extends Connector {
	public final static int MAX_LEN = 500;
	
	@Override
	public void init(String host, int port) {
		channelIn = new ChannelTest();
		channelOut = new ChannelTest();
	}
	
	private class ChannelTest implements Channel {
	
		@Override
		public void addShutdownListener(ShutdownListener arg0) {
			// TODO Auto-generated method stub
		}
	
		@Override
		public ShutdownSignalException getCloseReason() {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public boolean isOpen() {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public void notifyListeners() {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void removeShutdownListener(ShutdownListener arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void abort() throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void abort(int arg0, String arg1) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void addConfirmListener(ConfirmListener arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		@Deprecated
		public void addFlowListener(FlowListener arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void addReturnListener(ReturnListener arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void asyncRpc(Method arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void basicAck(long arg0, boolean arg1) throws IOException {
			Main.logger.log(-1, "ACK on : " + arg0);
		}
	
		@Override
		public void basicCancel(String arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public String basicConsume(String arg0, Consumer arg1) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public String basicConsume(String arg0, boolean arg1, Consumer arg2) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public String basicConsume(String arg0, boolean arg1, Map<String, Object> arg2, Consumer arg3) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public String basicConsume(String arg0, boolean arg1, String arg2, Consumer arg3) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public String basicConsume(String arg0, boolean arg1, String arg2, boolean arg3, boolean arg4,
				Map<String, Object> arg5, Consumer arg6) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public GetResponse basicGet(String arg0, boolean arg1) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void basicNack(long arg0, boolean arg1, boolean arg2) throws IOException {
			Main.logger.log(-1, "NACK on : " + arg0);
		}
	
		@Override
		public void basicPublish(String arg0, String arg1, BasicProperties arg2, byte[] arg3) throws IOException {
			String msg = (new String(arg3,"UTF-8"));
			Main.logger.log(-1, "Sent : " + msg.substring(0, msg.length()>MAX_LEN?MAX_LEN:msg.length()));
			Main.logger.log(-1, "On : " + arg1);
		}
	
		@Override
		public void basicPublish(String arg0, String arg1, boolean arg2, BasicProperties arg3, byte[] arg4)
				throws IOException {
			basicPublish(arg0, arg1, arg3, arg4);
		}
	
		@Override
		public void basicPublish(String arg0, String arg1, boolean arg2, boolean arg3, BasicProperties arg4, byte[] arg5)
				throws IOException {
			basicPublish(arg0, arg1, arg4, arg5);
		}
	
		@Override
		public void basicQos(int arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void basicQos(int arg0, boolean arg1) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void basicQos(int arg0, int arg1, boolean arg2) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public RecoverOk basicRecover() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public RecoverOk basicRecover(boolean arg0) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void basicReject(long arg0, boolean arg1) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void clearConfirmListeners() {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		@Deprecated
		public void clearFlowListeners() {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void clearReturnListeners() {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void close() throws IOException, TimeoutException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void close(int arg0, String arg1) throws IOException, TimeoutException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public SelectOk confirmSelect() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public BindOk exchangeBind(String arg0, String arg1, String arg2) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public BindOk exchangeBind(String arg0, String arg1, String arg2, Map<String, Object> arg3) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void exchangeBindNoWait(String arg0, String arg1, String arg2, Map<String, Object> arg3) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public DeclareOk exchangeDeclare(String arg0, String arg1) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public DeclareOk exchangeDeclare(String arg0, String arg1, boolean arg2) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public DeclareOk exchangeDeclare(String arg0, String arg1, boolean arg2, boolean arg3, Map<String, Object> arg4)
				throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public DeclareOk exchangeDeclare(String arg0, String arg1, boolean arg2, boolean arg3, boolean arg4,
				Map<String, Object> arg5) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void exchangeDeclareNoWait(String arg0, String arg1, boolean arg2, boolean arg3, boolean arg4,
				Map<String, Object> arg5) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public DeclareOk exchangeDeclarePassive(String arg0) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public DeleteOk exchangeDelete(String arg0) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public DeleteOk exchangeDelete(String arg0, boolean arg1) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void exchangeDeleteNoWait(String arg0, boolean arg1) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public UnbindOk exchangeUnbind(String arg0, String arg1, String arg2) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public UnbindOk exchangeUnbind(String arg0, String arg1, String arg2, Map<String, Object> arg3) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void exchangeUnbindNoWait(String arg0, String arg1, String arg2, Map<String, Object> arg3)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		@Deprecated
		public boolean flowBlocked() {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public int getChannelNumber() {
			// TODO Auto-generated method stub
			return 0;
		}
	
		@Override
		public Connection getConnection() {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Consumer getDefaultConsumer() {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public long getNextPublishSeqNo() {
			// TODO Auto-generated method stub
			return 0;
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Queue.BindOk queueBind(String arg0, String arg1, String arg2) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Queue.BindOk queueBind(String arg0, String arg1, String arg2,
				Map<String, Object> arg3) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void queueBindNoWait(String arg0, String arg1, String arg2, Map<String, Object> arg3) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Queue.DeclareOk queueDeclare() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Queue.DeclareOk queueDeclare(String arg0, boolean arg1, boolean arg2, boolean arg3,
				Map<String, Object> arg4) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void queueDeclareNoWait(String arg0, boolean arg1, boolean arg2, boolean arg3, Map<String, Object> arg4)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Queue.DeclareOk queueDeclarePassive(String arg0) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Queue.DeleteOk queueDelete(String arg0) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Queue.DeleteOk queueDelete(String arg0, boolean arg1, boolean arg2)
				throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void queueDeleteNoWait(String arg0, boolean arg1, boolean arg2) throws IOException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public PurgeOk queuePurge(String arg0) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Queue.UnbindOk queueUnbind(String arg0, String arg1, String arg2)
				throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Queue.UnbindOk queueUnbind(String arg0, String arg1, String arg2,
				Map<String, Object> arg3) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public boolean removeConfirmListener(ConfirmListener arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		@Deprecated
		public boolean removeFlowListener(FlowListener arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public boolean removeReturnListener(ReturnListener arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public Command rpc(Method arg0) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void setDefaultConsumer(Consumer arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public CommitOk txCommit() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public RollbackOk txRollback() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public com.rabbitmq.client.AMQP.Tx.SelectOk txSelect() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public boolean waitForConfirms() throws InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public boolean waitForConfirms(long arg0) throws InterruptedException, TimeoutException {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public void waitForConfirmsOrDie() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void waitForConfirmsOrDie(long arg0) throws IOException, InterruptedException, TimeoutException {
			// TODO Auto-generated method stub
			
		}
	
	}
}
