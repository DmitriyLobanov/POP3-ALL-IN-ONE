
public final class ClientServiceContext
    extends FSMContext
{
//---------------------------------------------------------------
// Member methods.
//

    public ClientServiceContext(ClientService owner)
    {
        super();

        _owner = owner;
        setState(ClientServiceFSM.NOT_IDENTIFIED);
        ClientServiceFSM.NOT_IDENTIFIED.Entry(this);
    }

    public synchronized void Delete(String msg)
    {
        _transition = "Delete";
        getState().Delete(this);
        _transition = "";
        return;
    }

    public synchronized void List(String arg)
    {
        _transition = "List";
        getState().List(this);
        _transition = "";
        return;
    }

    public synchronized void Noop()
    {
        _transition = "Noop";
        getState().Noop(this);
        _transition = "";
        return;
    }

    public synchronized void Pass(String pass)
    {
        _transition = "Pass";
        getState().Pass(this);
        _transition = "";
        return;
    }

    public synchronized void Quit()
    {
        _transition = "Quit";
        getState().Quit(this);
        _transition = "";
        return;
    }

    public synchronized void Retr(String msg)
    {
        _transition = "Retr";
        getState().Retr(this);
        _transition = "";
        return;
    }

    public synchronized void Rset()
    {
        _transition = "Rset";
        getState().Rset(this);
        _transition = "";
        return;
    }

    public synchronized void Stat()
    {
        _transition = "Stat";
        getState().Stat(this);
        _transition = "";
        return;
    }

    public synchronized void User(String arg)
    {
        _transition = "User";
        getState().User(this);
        _transition = "";
        return;
    }

    public ClientServiceState getState()
        throws StateUndefinedException
    {
        if (_state == null)
        {
            throw(
                new StateUndefinedException());
        }

        return ((ClientServiceState) _state);
    }

    protected ClientService getOwner()
    {
        return (_owner);
    }

//---------------------------------------------------------------
// Member data.
//

    transient private ClientService _owner;

//---------------------------------------------------------------
// Inner classes.
//

    public static abstract class ClientServiceState
        extends State
    {
    //-----------------------------------------------------------
    // Member methods.
    //

        protected ClientServiceState(String name, int id)
        {
            super (name, id);
        }

        protected void Entry(ClientServiceContext context) {}
        protected void Exit(ClientServiceContext context) {}

        protected void Delete(ClientServiceContext context)
        {
            Default(context);
        }

        protected void List(ClientServiceContext context)
        {
            Default(context);
        }

        protected void Noop(ClientServiceContext context)
        {
            Default(context);
        }

        protected void Pass(ClientServiceContext context)
        {
            Default(context);
        }

        protected void Quit(ClientServiceContext context)
        {
            Default(context);
        }

        protected void Retr(ClientServiceContext context)
        {
            Default(context);
        }

        protected void Rset(ClientServiceContext context)
        {
            Default(context);
        }

        protected void Stat(ClientServiceContext context)
        {
            Default(context);
        }

        protected void User(ClientServiceContext context)
        {
            Default(context);
        }

        protected void Default(ClientServiceContext context)
        {
            throw (
                new TransitionUndefinedException(
                    "State: " +
                    context.getState().getName() +
                    ", Transition: " +
                    context.getTransition()));
        }

    //-----------------------------------------------------------
    // Member data.
    //
    }

    /* package */ static abstract class ClientServiceFSM
    {
    //-----------------------------------------------------------
    // Member methods.
    //

    //-----------------------------------------------------------
    // Member data.
    //

        //-------------------------------------------------------
        // Statics.
        //
        public static ClientServiceFSM_Default.ClientServiceFSM_AUTHENTICATED AUTHENTICATED;
        public static ClientServiceFSM_Default.ClientServiceFSM_WAITING_NEW_CONNECTION WAITING_NEW_CONNECTION;
        public static ClientServiceFSM_Default.ClientServiceFSM_NOT_AUTHENTICATED NOT_AUTHENTICATED;
        public static ClientServiceFSM_Default.ClientServiceFSM_NOT_IDENTIFIED NOT_IDENTIFIED;
        private static ClientServiceFSM_Default Default;

        static
        {
            AUTHENTICATED = new ClientServiceFSM_Default.ClientServiceFSM_AUTHENTICATED("ClientServiceFSM.AUTHENTICATED", 12);
            WAITING_NEW_CONNECTION = new ClientServiceFSM_Default.ClientServiceFSM_WAITING_NEW_CONNECTION("ClientServiceFSM.WAITING_NEW_CONNECTION", 13);
            NOT_AUTHENTICATED = new ClientServiceFSM_Default.ClientServiceFSM_NOT_AUTHENTICATED("ClientServiceFSM.NOT_AUTHENTICATED", 14);
            NOT_IDENTIFIED = new ClientServiceFSM_Default.ClientServiceFSM_NOT_IDENTIFIED("ClientServiceFSM.NOT_IDENTIFIED", 15);
            Default = new ClientServiceFSM_Default("ClientServiceFSM.Default", -1);
        }

    }

    protected static class ClientServiceFSM_Default
        extends ClientServiceState
    {
    //-----------------------------------------------------------
    // Member methods.
    //

        protected ClientServiceFSM_Default(String name, int id)
        {
            super (name, id);
        }

    //-----------------------------------------------------------
    // Inner classse.
    //


        private static final class ClientServiceFSM_AUTHENTICATED
            extends ClientServiceFSM_Default
        {
        //-------------------------------------------------------
        // Member methods.
        //

            private ClientServiceFSM_AUTHENTICATED(String name, int id)
            {
                super (name, id);
            }

            protected void Delete(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.doDelete(ctxt.getArg());
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void List(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.doList(ctxt.getArg());
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Noop(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.doNoop();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Pass(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Quit(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();


                (context.getState()).Exit(context);
                context.clearState();
                try
                {
                    ctxt.doQuit();
                }
                finally
                {
                    context.setState(ClientServiceFSM.WAITING_NEW_CONNECTION);
                    (context.getState()).Entry(context);
                }
                return;
            }

            protected void Retr(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.doRetr(ctxt.getArg());
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Rset(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.doRset();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Stat(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.doStat();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void User(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

        //-------------------------------------------------------
        // Member data.
        //
        }

        private static final class ClientServiceFSM_WAITING_NEW_CONNECTION
            extends ClientServiceFSM_Default
        {
        //-------------------------------------------------------
        // Member methods.
        //

            private ClientServiceFSM_WAITING_NEW_CONNECTION(String name, int id)
            {
                super (name, id);
            }

        //-------------------------------------------------------
        // Member data.
        //
        }

        private static final class ClientServiceFSM_NOT_AUTHENTICATED
            extends ClientServiceFSM_Default
        {
        //-------------------------------------------------------
        // Member methods.
        //

            private ClientServiceFSM_NOT_AUTHENTICATED(String name, int id)
            {
                super (name, id);
            }

            protected void Delete(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void List(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Noop(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Pass(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();
                (context.getState()).Exit(context);
                ClientServiceState tmp = context.getState();
                context.clearState();
                int code = -1;
                try
                {
                    code = ctxt.doPass(ctxt.getArg());
                }
                finally
                {
                    if (code == 1) {
                        context.setState(ClientServiceFSM.AUTHENTICATED);
                    } else {
                        context.setState(tmp);
                        (context.getState()).Entry(context);
                    }
                }
                return;
            }

            protected void Quit(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();


                (context.getState()).Exit(context);
                context.clearState();
                try
                {
                    ctxt.doQuit();
                }
                finally
                {
                    context.setState(ClientServiceFSM.WAITING_NEW_CONNECTION);
                    (context.getState()).Entry(context);
                }
                return;
            }

            protected void Retr(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Rset(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Stat(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void User(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

        //-------------------------------------------------------
        // Member data.
        //
        }

        private static final class ClientServiceFSM_NOT_IDENTIFIED
            extends ClientServiceFSM_Default
        {
        //-------------------------------------------------------
        // Member methods.
        //

            private ClientServiceFSM_NOT_IDENTIFIED(String name, int id)
            {
                super (name, id);
            }

            protected void Delete(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void List(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Noop(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Pass(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Quit(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();


                (context.getState()).Exit(context);
                context.clearState();
                try
                {
                    ctxt.doQuit();
                }
                finally
                {
                    context.setState(ClientServiceFSM.WAITING_NEW_CONNECTION);
                    (context.getState()).Entry(context);
                }
                return;
            }

            protected void Retr(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Rset(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void Stat(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();

                ClientServiceState endState = context.getState();

                context.clearState();
                try
                {
                    ctxt.ERR();
                }
                finally
                {
                    context.setState(endState);
                }
                return;
            }

            protected void User(ClientServiceContext context)
            {
                ClientService ctxt = context.getOwner();


                (context.getState()).Exit(context);
                State tmp = context.getState();
                context.clearState();
                int code =-1;
                try
                {
                        code =  ctxt.doUser(ctxt.getArg());
                }
                finally
                {
                    if (code ==1) {
                        context.setState(ClientServiceFSM.NOT_AUTHENTICATED);
                    } else {
                        context.setState(tmp);
                        (context.getState()).Entry(context);}
                }
                return;
            }

        //-------------------------------------------------------
        // Member data.
        //
        }

    //-----------------------------------------------------------
    // Member data.
    //
    }
}
